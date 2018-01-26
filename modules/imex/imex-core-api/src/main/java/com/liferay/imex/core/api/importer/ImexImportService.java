package com.liferay.imex.core.api.importer;

import java.util.List;

public interface ImexImportService {
	
	public void doImportAll();
	
	public void doImport(List<String> bundleNames);

	public void doImport(String... bundleNames);
}
