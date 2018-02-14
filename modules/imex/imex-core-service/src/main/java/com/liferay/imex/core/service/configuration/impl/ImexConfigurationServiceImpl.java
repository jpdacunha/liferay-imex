package com.liferay.imex.core.service.configuration.impl;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.util.configuration.ImExPropsValues;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
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
	public Properties loadCoreConfiguration() {
		
		// The core configuration in IMEX is stored in the current core bundle
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		return loadConfiguration(bundle, IMEX);
		
	}

	@Override
	public Properties loadExporterConfiguration(Bundle bundle) {
		
		return loadConfiguration(bundle, EXPORTER);
		
	}
	
	@Override
	public Properties loadImporterConfiguration(Bundle bundle) {
		
		return loadConfiguration(bundle, IMPORTER);
		
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
	
	private Properties loadConfiguration(Bundle bundle, String type) {
		
		Properties props = loadFileSystemConfiguration(bundle, type);
		
		if (props == null) {
			props = getDefaultConfiguration(bundle);
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
			
			Properties props = loadExporterConfiguration(bundle);
			
			bundleConf.put(bundle.getSymbolicName(), props);
			
		}
		
		return bundleConf;
		
	}
	
	private Map<String,Properties> loadCoreConfigurationMap() {
		
		Map<String,Properties> bundleConf = new HashMap<>();
		Properties coreProps = loadCoreConfiguration();
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());		
		bundleConf.put(bundle.getSymbolicName(), coreProps);
		
		return bundleConf;
		
	}
	
	private Properties loadFileSystemConfiguration(Bundle bundle, String type) {
		//TODO : JDA aller chercher les fichiers dans le répertoire deploy/imex/configuration
		return null;
	}
	
	private Properties getDefaultConfiguration(Bundle bundle) {
		
		Properties props = null;
		
		String fileName = DEFAULT_FILENAME_PREFIX + ".properties";
				
		URL fileURL = bundle.getResource(fileName);
		
		try {
			
			if (fileURL != null) {
				
				InputStream in = fileURL.openStream();
				
				props = new Properties();
				props.load(in);
				
				in.close();
				
		    } else {
		    	_log.debug("Resource [" + fileName + "] is null.");
		    }

			
		} catch (IOException e) {
			_log.error(e,e);
	    }
		
		
		if (props != null) {
			_log.debug(MessageUtil.getMessage(bundle, "is using default configuration loaded from his embedded [" + fileName + "]."));
		} else {
			_log.error(MessageUtil.getMessage(bundle, "has no default configuration to loads. Make sure a [" + fileName + "] exists on your classpath."));
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
