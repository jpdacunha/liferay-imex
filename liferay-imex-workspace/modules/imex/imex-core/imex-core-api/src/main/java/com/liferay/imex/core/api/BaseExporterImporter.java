package com.liferay.imex.core.api;

import com.liferay.imex.core.api.deploy.Deployable;

/**
 * 
 * @author jpdacunha
 *
 */
public interface BaseExporterImporter extends Deployable {
	
	default public boolean isProfiled() {
		return false;
	}
	
	public String getRootDirectoryName();
	
	public String getProcessDescription();

}
