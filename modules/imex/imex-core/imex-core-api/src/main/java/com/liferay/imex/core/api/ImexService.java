package com.liferay.imex.core.api;

import java.util.List;

public interface ImexService {
	
	public String generateOverrideFileSystemConfigurationFiles();
	
	public String generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive);

}
