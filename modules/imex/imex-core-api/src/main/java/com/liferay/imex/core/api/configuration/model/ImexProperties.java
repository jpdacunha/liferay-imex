package com.liferay.imex.core.api.configuration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ImexProperties {
	
	private Properties properties = new Properties();

	private List<String> path = new ArrayList<>();
	
	public List<String> getPath() {
		return path;
	}

	public String addPath(String path) {
		this.getPath().add(path);
		return path;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
