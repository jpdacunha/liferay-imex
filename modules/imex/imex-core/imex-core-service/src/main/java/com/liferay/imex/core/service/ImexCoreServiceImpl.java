package com.liferay.imex.core.service;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.PropertyMergerService;
import com.liferay.imex.core.api.configuration.model.FileProperty;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.configuration.model.OrderedProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.service.configuration.model.ConfigurationOverrideProcessIdentifier;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.pmw.tinylog.LoggingContext;

@Component(
		immediate = true,
		service = ImexCoreService.class
	)
public class ImexCoreServiceImpl implements ImexCoreService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexCoreServiceImpl.class);
	
	private Lock imexCoreLock = null;
	
	private File imexTempDir;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexArchiverService imexArchiverService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected PropertyMergerService propertyMergerService;
	
	private ImporterTracker importerTrackerService;
	
	private ExporterTracker exporterTrackerService;
	
	@Override
	public String generateOverrideFileSystemConfigurationFiles() {
		return generateOverrideFileSystemConfigurationFiles(null, true);
	}
	
	@Override
	public void initializeLock() {
		
		synchronized(this) {
			
			if (this.imexCoreLock == null) {
				this.imexCoreLock = new ReentrantLock();
			} else if (tryLock()) {
				_log.warn("Unreleased lock detected. Imex Lock appear to be already locked. Trying to force lock to be released ...");
				releaseLock();
				_log.info("Lock successfully released");
			}
			
		}
	
	}
	
	@Override
	public void releaseLock() {
		this.imexCoreLock.unlock();
	}
	
	@Override
	public boolean tryLock() {
		return this.imexCoreLock.tryLock();
	}
	
	@Override
	public String generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive) {
		
		Map<String, ServiceReference<Importer>> importers = importerTrackerService.getFilteredImporters(bundleNames);
		Map<String, ServiceReference<Exporter>> exporters = exporterTrackerService.getFilteredExporters(bundleNames);		
		Map<String,Properties> props = configurationService.loadAllConfigurationMap(bundleNames, importers, exporters);
		
		ProcessIdentifierGenerator processIdentifier = new ConfigurationOverrideProcessIdentifier();
		
		String identifier = processIdentifier.getOrGenerateUniqueIdentifier();
		LoggingContext.put(ImexExecutionReportService.IDENTIFIER_KEY, identifier);
		
		reportService.getStartMessage(_log, "CFG_OVERRIDE process");
				
		//Initialisation répertoire de configuration
		initializeConfigurationtDirectories();
		
		if (props != null) {
			
			if (archive) {
				
				ImexProperties coreConfig = new ImexProperties();
				configurationService.loadCoreConfiguration(coreConfig);
				imexArchiverService.archiveCfgDirectory(coreConfig.getProperties(), processIdentifier);
				
			}
			
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				String key = entry.getKey();
				
				mergeConfiguration(props, key, bundle);
				
			}
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				String key = entry.getKey();
				
				mergeConfiguration(props, key, bundle);
				
			}
			
		}
		
		reportService.getEndMessage(_log, "CFG_OVERRIDE process");
		
		return identifier;
		
	}

	private void mergeConfiguration(Map<String, Properties> props, String entryKey, Bundle bundle) {
		
		//Loading relevant configuration to apply (this configuration contain all properties to apply)
		String bundleSymbolicName = bundle.getSymbolicName();
		
		try {
			
			//Configuration file does not exists
			File overridedPropsFile = configurationService.getConfigurationOverrideFileName(bundle);
			
			if (overridedPropsFile == null || (overridedPropsFile != null && !overridedPropsFile.exists())) {
			
				//Writing file with default datas
				configurationService.loadDefaultConfigurationInFile(bundle, overridedPropsFile);
				
				reportService.getOK(_log, entryKey, overridedPropsFile.getName(), overridedPropsFile, ImexOperationEnum.CREATE);
			
			} else {
				
				//Configuration file exists executing merge
				
				String tempFilesPrefix = ImExCorePropsKeys.IMEX_PREFIX + StringPool.MINUS + bundleSymbolicName + StringPool.MINUS;
				
				//Loading default configuration and saving it in temp file
				String defaultPropsFileName = tempFilesPrefix + "default" + StringPool.MINUS;		
				File defaultTempPropsFile = File.createTempFile(defaultPropsFileName, FileUtil.PROPERTIES_EXTENSION, getImexTempDir());
				configurationService.loadDefaultConfigurationInFile(bundle, defaultTempPropsFile);
				
				if (defaultTempPropsFile != null && defaultTempPropsFile.exists()) {
					
					//To file Property convertion
					OrderedProperties defaultOrderedProperty = new OrderedProperties();
					defaultOrderedProperty.load(defaultTempPropsFile);
					FileProperty defaultFileProperties = new FileProperty(defaultTempPropsFile, defaultOrderedProperty);
				
					//Loading relevant properties merged (embbeded in jar + default)
					Properties toApplyProperties  = props.get(bundleSymbolicName);
					
					//Creating temp File with content to merge
					String tempFileName = tempFilesPrefix + "overrided" + StringPool.MINUS;
					File toApplyPropertiesTempFile = File.createTempFile(tempFileName, FileUtil.PROPERTIES_EXTENSION, getImexTempDir());
					FileUtil.loadPropertiesInFile(toApplyPropertiesTempFile, toApplyProperties);
					
					OrderedProperties toApplyOrderedProperty = new OrderedProperties();
					toApplyOrderedProperty.load(toApplyPropertiesTempFile);
					FileProperty toApplyFileProperties = new FileProperty(toApplyPropertiesTempFile, toApplyOrderedProperty);
					
					//Call merge method Property merger
					propertyMergerService.merge(defaultFileProperties, toApplyFileProperties, overridedPropsFile);
					
					//Cleaning							
					if (toApplyPropertiesTempFile.exists()) {
						toApplyPropertiesTempFile.delete();
					}
					
					if (defaultTempPropsFile.exists()) {
						defaultTempPropsFile.delete();
					}
					
				} else {
					reportService.getDNE(_log, defaultTempPropsFile);
				}
				
				reportService.getOK(_log, entryKey, overridedPropsFile.getName(), overridedPropsFile, ImexOperationEnum.UPDATE);
				
			}
			
		} catch (IOException e) {
			 _log.error(e,e);
		}
	}
	
	@Activate
	private void initializeTempDirectory() {
		
		//Initialize configuration directory
		String tempFilePath = configurationService.getImexTempPath();
		
		File tempFile = new File(tempFilePath);
		
		tempFile.mkdirs();
		if (!tempFile.exists()) {
			_log.error("Failed to create directory " + tempFile);
		} else {
			this.imexTempDir = tempFile;
		}
		
		_log.info("Temporary directory is available at [" + getImexTempDir().getAbsolutePath() + "].");
		
	}
	
	public File getImexTempDir() {
		return imexTempDir;
	}

	private void initializeConfigurationtDirectories() {
		
		String cfgFilePath = configurationService.getImexCfgOverridePath();
		
		File cfgFile = new File(cfgFilePath);
		
		cfgFile.mkdirs();
		if (!cfgFile.exists()) {
			_log.error("Failed to create directory " + cfgFile);
		}
		

			
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(ImporterTracker trackerService) {
		this.importerTrackerService = trackerService;
	}

	protected void unsetImporterTracker(ImporterTracker trackerService) {
		this.importerTrackerService = null;
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.exporterTrackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.exporterTrackerService = null;
	}

}
