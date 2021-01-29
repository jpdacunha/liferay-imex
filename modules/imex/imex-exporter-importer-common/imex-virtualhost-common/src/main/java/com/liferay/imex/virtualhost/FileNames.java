package com.liferay.imex.virtualhost;


import com.liferay.petra.string.StringPool;

import java.security.acl.Group;

public class FileNames {
	
	public static final String DIR_VIRTUALHOST = "/virtualhost";
	public static final String VIRTUALHOST_FILENAME = "virtualhost";
	
	public static String getVirtualhostFileName(Group group, String extension) {
		return getVirtualhostFileNameBegin() + extension;
	}
	
	public static String getVirtualhostFileNameBegin() {
		return FileNames.VIRTUALHOST_FILENAME + StringPool.MINUS;
	}

}
