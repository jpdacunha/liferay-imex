package com.liferay.imex.core.api.configuration.model;

import java.util.Map;
import java.util.Properties;

public class ImexProperties {
	
	private Properties properties = new Properties();

	private String path;
	
	private boolean bundleConfiguration;
	
	private boolean defaulConfiguration;

	public boolean isDefaulConfiguration() {
		return defaulConfiguration;
	}

	public void setDefaulConfiguration(boolean defaulConfiguration) {
		this.defaulConfiguration = defaulConfiguration;
	}

	public boolean isBundleConfiguration() {
		return bundleConfiguration;
	}

	public void setBundleConfiguration(boolean bundleConfiguration) {
		this.bundleConfiguration = bundleConfiguration;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void putAll(Map<? extends Object, ? extends Object> t) {
		properties.putAll(t);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
