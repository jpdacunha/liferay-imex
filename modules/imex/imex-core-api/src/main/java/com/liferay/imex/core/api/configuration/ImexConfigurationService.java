package com.liferay.imex.core.api.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexConfigurationService {
	
	public Properties loadExporterConfiguration(Bundle bundle);
	
	public Properties loadImporterConfiguration(Bundle bundle);
	
	public Properties loadCoreConfiguration();
	
	public String getImexPath();
	
	public String getImexDataPath(); 
	
	public String getImexArchivePath();
	
	public String getImexCfgOverridePath();
	
	public Map<String,Properties> loadAllConfigurationMap(List<String> bundleNames);

	public File getConfigurationOverrideFileName(Bundle bundle);

	public File getConfigurationOverrideFileName(Entry<String, Properties> entry);
	
}
