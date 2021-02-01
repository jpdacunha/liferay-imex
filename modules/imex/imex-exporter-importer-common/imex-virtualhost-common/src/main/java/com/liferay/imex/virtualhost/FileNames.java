package com.liferay.imex.virtualhost;


import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.VirtualHost;


public class FileNames {
	
	public static final String DIR_VIRTUALHOST = "/virtualhost";
	public static final String VIRTUALHOST_FILENAME = "virtualhost";
	
	public static String getVirtualhostFileName(VirtualHost virtualHost, Company company, Group group,  String extension) {
		
		String groupName = StringPool.BLANK;
		if (group != null) {
			groupName = StringPool.MINUS + ImexNormalizer.getDirNameByFriendlyURL(group.getFriendlyURL());
		}
		
		return getVirtualhostFileNameBegin() + ImexNormalizer.convertToKey(company.getWebId()) + groupName + StringPool.MINUS + ImexNormalizer.convertToKey(virtualHost.getHostname()) + extension;
	}
	
	public static String getVirtualhostFileNameBegin() {
		return FileNames.VIRTUALHOST_FILENAME + StringPool.MINUS;
	}

}
