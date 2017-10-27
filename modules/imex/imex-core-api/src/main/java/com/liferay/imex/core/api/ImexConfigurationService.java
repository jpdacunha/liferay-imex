package com.liferay.imex.core.api;

import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexConfigurationService {
	
	public Properties loadExporterConfiguration(Bundle bundle);
	
	public Properties loadImporterConfiguration(Bundle bundle);
	
	public String getImexPath();
	
	public String getImexDataPath(); 

}
