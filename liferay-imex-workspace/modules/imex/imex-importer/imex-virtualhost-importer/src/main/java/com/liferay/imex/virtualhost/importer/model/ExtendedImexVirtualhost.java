package com.liferay.imex.virtualhost.importer.model;

import com.liferay.imex.virtualhost.model.ImexVirtualhost;

import java.io.File;

public class ExtendedImexVirtualhost {
	
	private File sourceFile;
	
	private ImexVirtualhost virtualhost;
	
	public ExtendedImexVirtualhost(File sourceFile, ImexVirtualhost virtualhost) {
		super();
		this.sourceFile = sourceFile;
		this.virtualhost = virtualhost;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public ImexVirtualhost getVirtualhost() {
		return virtualhost;
	}

	public void setVirtualhost(ImexVirtualhost virtualhost) {
		this.virtualhost = virtualhost;
	}

	public boolean isDefaultVirtualHost() {
		return virtualhost.isDefaultVirtualHost();
	}
	
	

}
