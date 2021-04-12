package com.liferay.imex.core.service;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.PropertyMergerService;
import com.liferay.imex.core.api.configuration.model.FileProperty;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.configuration.model.OrderedProperties;
import com.liferay.imex.core.api.deploy.DeployDirEnum;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.api.trigger.Trigger;
import com.liferay.imex.core.api.trigger.TriggerTracker;
import com.liferay.imex.core.service.configuration.model.ConfigurationOverrideProcessIdentifier;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
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
	
	private File imexWorkDir;

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
	
	private TriggerTracker triggerTrackerService;
	
	@Override
	public void cleanBundleFiles(DeployDirEnum destinationDirName, String toCopyBundleDirectoryName, Bundle bundle) {
		
		if (bundle != null) {
			
			if (destinationDirName != null) {
				
				List<URL> urls = FileUtil.findBundleResources(bundle, toCopyBundleDirectoryName, StringPool.STAR);
				
				if (urls != null && urls.size() > 0) {
											
					String imexHomePath = configurationService.getImexPath();
					
					String rootPath = imexHomePath + StringPool.SLASH + destinationDirName.getDirectoryPath();
					String bundleDirectoryPath = rootPath + StringPool.SLASH + bundle.getSymbolicName();
					
					File destinationDir = new File(bundleDirectoryPath);
					if (destinationDir.exists()) {
						
						FileUtil.deleteDirectory(destinationDir);
						
						File rootDir = new File(rootPath);
						
						//Deleting root dir if empty
						if (rootDir != null && rootDir.exists()) {
							
							try {
								
								if (FileUtil.isEmptyDirectory(rootDir)) {
									FileUtil.deleteDirectory(rootDir);
								} else {
									_log.debug("rootDirectory is not empty");
								}
								
							} catch (ImexException e) {
								_log.error(e,e);
							} catch (IOException e) {
								_log.error(e,e);
							}
							
						} else {
							_log.debug("rootDirectory is null or it's does not exists");
						}
									
					} else {
						_log.info("[" + destinationDir.getAbsolutePath() + "] does not exists. Nothing to clean.");
					}
					
				} else {
					_log.debug("No files found to copy");
				}
				
			} else {
				_log.warn("Missing required parameter [destinationDirName]");
			}
			
		} else {
			_log.warn("Missing required parameter [bundle]");		
		}
	}
	
	@Override
	public void deployBundleFiles(DeployDirEnum destinationDirName, String toCopyBundleDirectoryName, Bundle bundle) {
		
		if (bundle != null) {
			
			if (destinationDirName != null) {
				
				List<URL> urls = FileUtil.findBundleResources(bundle, toCopyBundleDirectoryName, StringPool.STAR);
				
				if (urls != null && urls.size() > 0) {
					
					try {
						
						String imexHomePath = configurationService.getImexPath();
						String bundleDirectoryPath = imexHomePath + StringPool.SLASH + destinationDirName.getDirectoryPath() + StringPool.SLASH + bundle.getSymbolicName();
						
						File destinationDir = FileUtil.initializeDirectory(bundleDirectoryPath);
						FileUtil.copyUrlsAsFiles(destinationDir, urls);
						
						setFilesPermissions(destinationDir);
						
					} catch (ImexException e) {
						_log.error(e,e);
					}
					
				} else {
					_log.debug("No files found to copy");
				}
				
			} else {
				_log.warn("Missing required parameter [destinationDirName]");
			}
			
		} else {
			_log.warn("Missing required parameter [bundle]");
		}
		
	}

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
		Map<String, ServiceReference<Trigger>>  triggers = triggerTrackerService.getTriggers();
		
		Map<String,Properties> props = configurationService.loadAllConfigurationMap(bundleNames, importers, exporters, triggers);
		
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
			
			Bundle coreBundle = FrameworkUtil.getBundle(this.getClass());
			String coreBundleName = coreBundle.getSymbolicName();
			mergeConfiguration(props, coreBundleName, coreBundle);
			
			//Merging importers
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				String key = entry.getKey();
				
				mergeConfiguration(props, key, bundle);
				
			}
			
			//Merging exporters
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				String key = entry.getKey();
				
				mergeConfiguration(props, key, bundle);
				
			}
			
			//Merging triggers
			for (Map.Entry<String ,ServiceReference<Trigger>> entry  : triggers.entrySet()) {
				
				ServiceReference<Trigger> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				String key = entry.getKey();
				
				mergeConfiguration(props, key, bundle);
				
			}
			
		}
		
		reportService.getEndMessage(_log, "CFG_OVERRIDE process");
		
		return identifier;
		
	}
	
	private void setFilesPermissions(File destinationDir) throws ImexException {
		
		File[] files = destinationDir.listFiles();
		
		if (files != null && files.length > 0) {
			
			ImexProperties imexProps = new ImexProperties();
			configurationService.loadCoreConfiguration(imexProps);
			
			Properties props = imexProps.getProperties();
			
			List<String> readExtensions = CollectionUtil.getList(props.getProperty(ImExCorePropsKeys.DEPLOYER_READ_PERMISSION_FILES_EXTENSIONS));
			List<String>  writeExtensions = CollectionUtil.getList(props.getProperty(ImExCorePropsKeys.DEPLOYER_WRITE_PERMISSION_FILES_EXTENSIONS));
			List<String> executeExtensions = CollectionUtil.getList(props.getProperty(ImExCorePropsKeys.DEPLOYER_EXECUTE_PERMISSION_FILES_EXTENSIONS));

			for (File file : files) {
				
				String extension = FileUtil.getExtension(file);
				
				if (Validator.isNotNull(extension)) {
				
					boolean readable = (readExtensions != null && readExtensions.contains(extension));
					boolean writable = (writeExtensions != null && writeExtensions.contains(extension));
					boolean executable = (executeExtensions != null && executeExtensions.contains(extension));
					
					FileUtil.setFilePermissions(file, writable, readable, executable);
					
				} else {
					_log.warn("Unable to set permission for file [" + file.getAbsolutePath() + "] because his extension is undefined.");
				}
				
			}
			
		} else {
			
			_log.debug("Directory is empty");
			
		}
		
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
	
	public File getImexTempDir() {
		return imexTempDir;
	}
	
	public File getImexWorkDir() {
		return imexWorkDir;
	}
	
	@Activate
	private void initialize() {
		_log.info("Starting imex core initialization ...");
		initializeTempDirectory();
		initializeWorkDirectory();
		_log.info("Initialization done.");
	}
	
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
	
	private void initializeWorkDirectory() {
		
		//Initialize configuration directory
		String filePath = configurationService.getImexWorkPath();
		
		File file = new File(filePath);
		
		file.mkdirs();
		if (!file.exists()) {
			_log.error("Failed to create directory " + file);
		} else {
			this.imexWorkDir = file;
		}
		
		_log.info("Work directory is available at [" + getImexWorkDir().getAbsolutePath() + "].");
		
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
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setTriggerTracker(TriggerTracker trackerService) {
		this.triggerTrackerService = trackerService;
	}

	protected void unsetTriggerTracker(TriggerTracker trackerService) {
		this.triggerTrackerService = null;
	}

}
