package com.liferay.imex.core.api.configuration.model;

import java.io.File;

public class FileProperty {
	
	private File originalFile;
	private OrderedProperties properties;
	
	public FileProperty(File originalFile, OrderedProperties properties) {
		super();
		this.originalFile = originalFile;
		this.properties = properties;
	}
	
	public File getOriginalFile() {
		return originalFile;
	}
	public void setOriginalFile(File originalFile) {
		this.originalFile = originalFile;
	}
	public OrderedProperties getProperties() {
		return properties;
	}
	public void setProperties(OrderedProperties properties) {
		this.properties = properties;
	}

}
