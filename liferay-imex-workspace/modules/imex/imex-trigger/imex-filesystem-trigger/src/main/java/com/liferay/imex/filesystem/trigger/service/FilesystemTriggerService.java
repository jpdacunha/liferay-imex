package com.liferay.imex.filesystem.trigger.service;

public interface FilesystemTriggerService {

	public void createMissingFiles();
	
	public void cleanFiles();

	public void executeImex();

}
