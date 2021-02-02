package com.liferay.imex.core.api;

/**
 * 
 * @author jpdacunha
 *
 */
public interface BaseExporterImporter {
	
	default public boolean isProfiled() {
		return false;
	}
	
	public String getRootDirectoryName();
	
	public String getProcessDescription();

}
