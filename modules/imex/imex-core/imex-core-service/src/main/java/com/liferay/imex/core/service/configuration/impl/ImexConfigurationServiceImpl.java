package com.liferay.imex.core.service.configuration.impl;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.util.configuration.ImExPropsValues;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate = true, service = ImexConfigurationService.class)
public class ImexConfigurationServiceImpl implements ImexConfigurationService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexConfigurationServiceImpl.class);
	
	public final static String EXPORTER = "exporter";
	public final static String IMPORTER = "importer";
	public final static String IMEX = "imex";
	
	private static final String DEFAULT_FILENAME_PREFIX = "default";
	
	private ImporterTracker importerTrackerService;
	
	private ExporterTracker exporterTrackerService;
	
	@Override
	public void loadCoreConfiguration(ImexProperties props) {
		
		// The core configuration in IMEX is stored in the current core bundle
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		loadConfiguration(bundle, props, IMEX);
		
	}

	@Override
	public void loadExporterConfiguration(Bundle bundle, ImexProperties props) {
		
		loadConfiguration(bundle, props, EXPORTER);
		
	}
	
	@Override
	public void loadExporterAndCoreConfiguration(Bundle bundle, ImexProperties props) {
		
		loadCoreConfiguration(props);
		
		loadExporterConfiguration(bundle, props);
		
	}
	
	@Override
	public void loadImporterConfiguration(Bundle bundle, ImexProperties props) {
		
		loadConfiguration(bundle, props, IMPORTER);
		
	}
	
	@Override
	public void loadImporterAndCoreConfiguration(Bundle bundle, ImexProperties props) {
		
		loadImporterConfiguration(bundle, props);
	
		loadCoreConfiguration(props);
		
	}
	
	@Override
	public Map<String,Properties> loadAllConfigurationMap(List<String> bundleNames) {
		
		Map<String,Properties> conf = new HashMap<>();
		
		//3 levels of configuration
		Map<String,Properties> coreConfig = loadCoreConfigurationMap();
		conf.putAll(coreConfig);
		
		Map<String,Properties> importerConfig = loadImportersConfigurationMap(bundleNames);
		conf.putAll(importerConfig);
		
		Map<String,Properties> exporterConfig = loadExportersConfigurationMap(bundleNames);
		conf.putAll(exporterConfig);
		
		return conf;
	
	}
	
	@Override
	public File getConfigurationOverrideFileName(Bundle bundle) {
		
		if (bundle != null) {
			String fileName = bundle.getSymbolicName() + ".properties";
			
			return new File(this.getImexCfgOverridePath() + StringPool.SLASH + fileName);
		} else {
			_log.error("Bundle is null ...");
		}
	
		return null;
		
	}
	
	@Override
	public File getConfigurationOverrideFileName(Entry<String, Properties> entry) {
		
		if (entry != null) {
			String fileName = entry.getKey() + ".properties";
			
			return new File(this.getImexCfgOverridePath() + StringPool.SLASH + fileName);
		} else {
			_log.error("Bundle is null ...");
		}
	
		return null;
		
	}
	
	private ImexProperties loadConfiguration(Bundle bundle, ImexProperties props, String type) {
		
		loadFileSystemConfiguration(bundle, props, type);
		
		if (props != null) {
			
			if (props.getProperties().size() == 0) {
				
				props = loadDefaultConfiguration(bundle, props);
				
			}
			
		} else {
			_log.error("Properties are null");
		}

		return props;
		
	}
	
	private Map<String,Properties> loadImportersConfigurationMap(List<String> bundleNames) {
		
		Map<String, ServiceReference<Importer>> importers = importerTrackerService.getFilteredImporters(bundleNames);
		return loadConfigurationMap(bundleNames, importers);
		
	}
	
	private Map<String,Properties> loadExportersConfigurationMap(List<String> bundleNames) {
		
		Map<String, ServiceReference<Exporter>> exporters = exporterTrackerService.getFilteredExporters(bundleNames);
		return loadConfigurationMap(bundleNames, exporters);
		
	}
	
	private <E> Map<String,Properties> loadConfigurationMap(List<String> bundleNames, Map<String, ServiceReference<E>> references) {
		
		Map<String,Properties> bundleConf = new HashMap<>();
		
		for (Map.Entry<String ,ServiceReference<E>> entry  : references.entrySet()) {
			
			ServiceReference<E> reference = entry.getValue();
			
			Bundle bundle = reference.getBundle();
			
			ImexProperties props = new ImexProperties();
			loadExporterConfiguration(bundle, props);
			
			bundleConf.put(bundle.getSymbolicName(), props.getProperties());
			
		}
		
		return bundleConf;
		
	}
	
	private Map<String,Properties> loadCoreConfigurationMap() {
		
		Map<String,Properties> bundleConf = new HashMap<>();
		ImexProperties props = new ImexProperties();
		loadCoreConfiguration(props);
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());		
		bundleConf.put(bundle.getSymbolicName(), props.getProperties());
		
		return bundleConf;
		
	}
	
	private ImexProperties loadFileSystemConfiguration(Bundle bundle, ImexProperties props, String type) {
		
		File overrideCfgFile = getConfigurationOverrideFileName(bundle);
		
		if (overrideCfgFile != null && overrideCfgFile.exists()) {
			
			try {
				
				props.getProperties().load(new FileInputStream(overrideCfgFile));
				props.addPath(overrideCfgFile.getAbsolutePath());
				
			} catch (IOException e) {
				_log.error(e,e);
			}
		}
		
		return props;
		
	}
	
	private ImexProperties loadDefaultConfiguration(Bundle bundle, ImexProperties props) {
		
		String fileName = DEFAULT_FILENAME_PREFIX + ".properties";
			
		URL fileURL = bundle.getResource(fileName);
		
		try {
			
			if (fileURL != null) {
				
				InputStream in = null;
				try {
					
					in = fileURL.openStream();
					props.getProperties().load(in);
					props.addPath("classpath:" + fileName);
					
				} finally {
					if (in != null) {
						in.close();
					}
				}
				
		    } else {
		    	_log.debug("Resource [" + fileName + "] is null.");
		    }

			
		} catch (IOException e) {
			_log.error(e,e);
	    } 
		
		return props;
		
	}

	@Override
	public String getImexPath() {
		return ImExPropsValues.DEPLOY_DIR + "/imex";
	}
	
	@Override
	public String getImexDataPath() {
		return getImexPath() + "/data";
	}
	
	@Override
	public String getImexLogsPath() {
		return getImexPath() + "/logs";
	}

	@Override
	public String getImexArchivePath() {
		return getImexPath() + "/archive";
	}
	
	@Override
	public String getImexCfgOverridePath() {
		return getImexPath() + "/configuration";
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
