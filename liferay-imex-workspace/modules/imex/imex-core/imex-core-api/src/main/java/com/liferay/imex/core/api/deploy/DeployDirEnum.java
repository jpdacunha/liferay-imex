package com.liferay.imex.core.api.deploy;

public enum DeployDirEnum {

	SCRIPTS("/scripts");
	
	private String directoryPath;	
	
	private DeployDirEnum(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

}
