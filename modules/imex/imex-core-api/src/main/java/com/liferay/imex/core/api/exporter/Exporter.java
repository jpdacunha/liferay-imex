package com.liferay.imex.core.api.exporter;

public interface Exporter {
	
	public void doExport();
	
	public String getDirectoryName();
	
	public String getProcessDescription();

}
