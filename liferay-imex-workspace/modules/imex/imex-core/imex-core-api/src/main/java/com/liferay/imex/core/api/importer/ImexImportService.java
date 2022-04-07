package com.liferay.imex.core.api.importer;

import java.util.List;

public interface ImexImportService {
	
	public String doImportAll();
	
	public String doImport(List<String> bundleNames);

	public String doImport(String... bundleNames);

	public String doImport(List<String> bundleNames, String profileId);
	
	public String doImport(List<String> bundleNames, String profileId, boolean isDebug);
	
}
