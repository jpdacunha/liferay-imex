package com.liferay.imex.core.api.exporter;

import java.util.List;

public interface ImexExportService {
	
	public String doExportAll();
	
	public String doExport(List<String> bundleNames);

	public String doExport(String... bundleNames);

	public String doExport(List<String> bundleNames, String profileId);
	
	public String doExport(List<String> bundleNames, String profileId, boolean isDebug);

}
