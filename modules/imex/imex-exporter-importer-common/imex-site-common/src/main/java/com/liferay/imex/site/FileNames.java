package com.liferay.imex.site;


import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;

import java.util.Locale;

public class FileNames {
	
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	public static final String DIR_SITE = "/site";
	public static final String SITE_DESCRIPTOR_FILE_PREFIX = "site-";
	
	public static String getSiteFileName(Group group, String extension) {		
		return getSiteFileName(group.getFriendlyURL(), extension);
	}
	
	public static String getSiteFileName(String friendlyUrl, String extension) {
		return SITE_DESCRIPTOR_FILE_PREFIX + ImexNormalizer.getDirNameByFriendlyURL(friendlyUrl) + extension;	
	}
	
	public static String getLarSiteFileName(Group group, boolean privateLayout, Locale locale) {
		return getSiteFileNameBegin(privateLayout) + ImexNormalizer.getDirNameByFriendlyURL(group.getFriendlyURL()) + "-layouts" + FileUtil.LAR_EXTENSION;
	}
	
	public static String getSiteFileNameBegin(boolean privateLayout) {
		return (privateLayout ? PRIVATE : PUBLIC) + StringPool.MINUS;
	}

}
