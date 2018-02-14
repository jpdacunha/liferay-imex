package com.liferay.imex.core.api;

import java.util.List;

public interface ImexService {
	
	public void generateOverrideFileSystemConfigurationFiles();
	
	public void generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive);

}
