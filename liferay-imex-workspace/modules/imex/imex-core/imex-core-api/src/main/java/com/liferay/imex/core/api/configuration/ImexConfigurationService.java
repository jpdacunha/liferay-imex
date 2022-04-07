package com.liferay.imex.core.api.configuration;

import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.trigger.Trigger;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public interface ImexConfigurationService {
	
	public void loadCoreConfiguration(ImexProperties props);
	
	public String getImexPath();
	
	public String getImexDataPath(); 
	
	public String getImexRawDataPath(); 
	
	public String getImexArchivePath();
	
	public String getImexCfgOverridePath();
	
	public String getImexLogsPath();
	
	public File getConfigurationOverrideFileName(Bundle bundle);

	public File getConfigurationOverrideFileName(Entry<String, Properties> entry);

	public void loadExporterAndCoreConfiguration(Bundle bundle, ImexProperties props);
	
	public void loadImporterAndCoreConfiguration(Bundle bundle, ImexProperties props);
	
	public String loadDefaultConfigurationAsString(Bundle bundle);

	public String getImexTempPath();

	public void loadDefaultConfigurationInFile(Bundle bundle, File file);

	public Map<String, Properties> loadAllConfigurationMap(List<String> bundleNames,
			Map<String, ServiceReference<Importer>> importers, Map<String, ServiceReference<Exporter>> exporters,
			Map<String, ServiceReference<Trigger>> triggers);

	public void loadExporterAndCoreConfiguration(Class<?> clazz, ImexProperties props);

	public void loadImporterAndCoreConfiguration(Class<?> clazz, ImexProperties props);

	public void loadTriggerAndCoreConfiguration(Bundle bundle, ImexProperties props);

	public void loadTriggerAndCoreConfiguration(Class<?> clazz, ImexProperties props);

	public String getImexWorkPath();
	
	public File getImexWorkFile();

}
