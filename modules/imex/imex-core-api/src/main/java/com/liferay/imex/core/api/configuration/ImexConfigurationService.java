package com.liferay.imex.core.api.configuration;

import com.liferay.imex.core.api.configuration.model.ImexProperties;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexConfigurationService {
	
	public ImexProperties loadExporterConfiguration(Bundle bundle);
	
	public ImexProperties loadImporterConfiguration(Bundle bundle);
	
	public ImexProperties loadCoreConfiguration();
	
	public String getImexPath();
	
	public String getImexDataPath(); 
	
	public String getImexArchivePath();
	
	public String getImexCfgOverridePath();
	
	public String getImexLogsPath();
	
	public Map<String,Properties> loadAllConfigurationMap(List<String> bundleNames);

	public File getConfigurationOverrideFileName(Bundle bundle);

	public File getConfigurationOverrideFileName(Entry<String, Properties> entry);

	public ImexProperties loadExporterAndCoreConfiguration(Bundle bundle);
	
	public ImexProperties loadImporterAndCoreConfiguration(Bundle bundle);
	
}
