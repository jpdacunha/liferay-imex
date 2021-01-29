package com.liferay.imex.core.api.exporter;

import com.liferay.portal.kernel.model.User;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

public interface Exporter {
	
	public void doExport(User user, Properties config, File destDir, long companyId, Locale locale, boolean debug);
	
	public String getProcessDescription();
	
	public String getExporterRootDirectory();
	
	default public boolean isProfiled() {
		return false;
	}
	
}
