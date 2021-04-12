package com.liferay.imex.core.api;

import com.liferay.imex.core.api.deploy.DeployDirEnum;

import java.util.List;

import org.osgi.framework.Bundle;

public interface ImexCoreService {
	
	public static final String LOCKED_MESSAGE = "It seems that Imex is already working for you. Please wait à minute and try again.";
	
	public String generateOverrideFileSystemConfigurationFiles();
	
	public String generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive);

	public void initializeLock();

	public void releaseLock();

	public boolean tryLock();

	public void deployBundleFiles(DeployDirEnum destinationDirName, String toCopyBundleDirectoryName, Bundle bundle);

	public void cleanBundleFiles(DeployDirEnum destinationDirName, String toCopyBundleDirectoryName, Bundle bundle);

}
