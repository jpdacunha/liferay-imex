package com.liferay.imex.core.api;

import java.util.List;

public interface ImexCoreService {
	
	public static final String LOCKED_MESSAGE = "It seems that Imex is already working for you. Please wait à minute and try again.";
	
	public String generateOverrideFileSystemConfigurationFiles();
	
	public String generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive);

	public void initializeLock();

	public void releaseLock();

	public boolean tryLock();

}
