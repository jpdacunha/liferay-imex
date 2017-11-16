package com.liferay.imex.core.api.importer;

import com.liferay.imex.core.api.ImexTask;

import java.io.File;
import java.util.Properties;

public interface Importer extends ImexTask {
	
	public void doImport(Properties config, File srcDir, long companyId, boolean debug);
	
	public String getProcessDescription();

}
