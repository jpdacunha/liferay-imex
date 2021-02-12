package com.liferay.imex.core.service.configuration.impl;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.importer.Importer;
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

@Component(immediate = true, service = ImexConfigurationService.class)
public class ImexConfigurationServiceImpl implements ImexConfigurationService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexConfigurationServiceImpl.class);
	
	private static final String CONFIGURATION = "/configuration";

	private static final String ARCHIVE = "/archive";

	private static final String LOGS = "/logs";

	private static final String DATA = "/data";
	
	private static final String RAW_DATA = "/data-raw";

	private static final String IMEX2 = "/imex";

	public final static String EXPORTER = "exporter";
	public final static String IMPORTER = "importer";
	public final static String IMEX = ImExCorePropsKeys.IMEX_PREFIX;
	
	private static final String DEFAULT_FILENAME_PREFIX = "default";
	
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
	public Map<String,Properties> loadAllConfigurationMap(List<String> bundleNames, Map<String, ServiceReference<Importer>> importers, Map<String, ServiceReference<Exporter>> exporters) {
		
		Map<String,Properties> conf = new HashMap<>();
		
		//3 levels of configuration
		Map<String,Properties> coreConfig = loadCoreConfigurationMap();
		conf.putAll(coreConfig);
		
		Map<String,Properties> importerConfig = loadImportersConfigurationMap(bundleNames, importers);
		conf.putAll(importerConfig);
		
		Map<String,Properties> exporterConfig = loadExportersConfigurationMap(bundleNames, exporters);
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
	
	private Map<String,Properties> loadImportersConfigurationMap(List<String> bundleNames, Map<String, ServiceReference<Importer>> importers) {
		
		//Map<String, ServiceReference<Importer>> importers = importerTrackerService.getFilteredImporters(bundleNames);
		return loadConfigurationMap(bundleNames, importers);
		
	}
	
	private Map<String,Properties> loadExportersConfigurationMap(List<String> bundleNames, Map<String, ServiceReference<Exporter>> exporters) {
		
		//Map<String, ServiceReference<Exporter>> exporters = exporterTrackerService.getFilteredExporters(bundleNames);
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
		return ImExPropsValues.DEPLOY_DIR + IMEX2;
	}
	
	@Override
	public String getImexDataPath() {
		return getImexPath() + DATA;
	}
	
	@Override
	public String getImexRawDataPath() {
		return getImexPath() + RAW_DATA;
	}
	
	@Override
	public String getImexLogsPath() {
		return getImexPath() + LOGS;
	}

	@Override
	public String getImexArchivePath() {
		return getImexPath() + ARCHIVE;
	}
	
	@Override
	public String getImexCfgOverridePath() {
		return getImexPath() + CONFIGURATION;
	}

}
