package com.liferay.imex.core.api.exporter;

import java.util.List;

public interface ImexExportService {
	
	public void doExportAll();
	
	public void doExport(List<String> bundleNames);

	public void doExport(String... bundleNames);

}
