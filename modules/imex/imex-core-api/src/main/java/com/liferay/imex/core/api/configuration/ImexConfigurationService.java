package com.liferay.imex.core.api.configuration;

import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexConfigurationService {
	
	public Properties loadExporterConfiguration(Bundle bundle);
	
	public Properties loadImporterConfiguration(Bundle bundle);
	
	public Properties loadCoreConfiguration();
	
	public String getImexPath();
	
	public String getImexDataPath(); 
	
	public String getImexArchivePath();

}
