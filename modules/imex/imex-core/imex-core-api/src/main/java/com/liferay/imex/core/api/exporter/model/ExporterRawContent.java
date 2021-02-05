package com.liferay.imex.core.api.exporter.model;

import java.io.Serializable;

public class ExporterRawContent implements Serializable {
	
	public ExporterRawContent(String fileName, String fileContent) {
		super();
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5859891042297219614L;

	private String fileName;
	
	private String fileContent;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

}
