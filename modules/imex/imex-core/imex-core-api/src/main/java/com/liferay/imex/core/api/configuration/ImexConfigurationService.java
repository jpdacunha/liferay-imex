package com.liferay.imex.core.api.configuration;

import com.liferay.imex.core.api.configuration.model.ImexProperties;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexConfigurationService {
	
	public void loadExporterConfiguration(Bundle bundle, ImexProperties props);
	
	public void loadImporterConfiguration(Bundle bundle, ImexProperties props);
	
	public void loadCoreConfiguration(ImexProperties props);
	
	public String getImexPath();
	
	public String getImexDataPath(); 
	
	public String getImexArchivePath();
	
	public String getImexCfgOverridePath();
	
	public String getImexLogsPath();
	
	public Map<String,Properties> loadAllConfigurationMap(List<String> bundleNames);

	public File getConfigurationOverrideFileName(Bundle bundle);

	public File getConfigurationOverrideFileName(Entry<String, Properties> entry);

	public void loadExporterAndCoreConfiguration(Bundle bundle, ImexProperties props);
	
	public void loadImporterAndCoreConfiguration(Bundle bundle, ImexProperties props);
	
}
