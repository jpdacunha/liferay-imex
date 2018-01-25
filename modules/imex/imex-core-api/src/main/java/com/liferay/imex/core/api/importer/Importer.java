package com.liferay.imex.core.api.importer;

import com.liferay.imex.core.api.ImexTask;
import com.liferay.portal.kernel.model.User;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

public interface Importer extends ImexTask {
	
	public void doImport(User exportUser, Properties config, File srcDir, long companyId, Locale locale, boolean debug);
	
	public String getProcessDescription();

}
