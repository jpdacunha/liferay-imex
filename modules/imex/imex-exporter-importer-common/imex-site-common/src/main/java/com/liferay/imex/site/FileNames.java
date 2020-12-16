package com.liferay.imex.site;


import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;

import java.util.Locale;

public class FileNames {
	
	private static final String PRIVATE = "Private";
	private static final String PUBLIC = "Public";
	public static final String DIR_SITE = "/site";
	public static final String SITE_FILENAME = "adt";
	
	public static String getSiteFileName(Group group, Locale locale, String extension) {
		return GroupUtil.getGroupName(group, locale) + extension;
	}
	
	public static String getLarSiteFileName(Group group, boolean privateLayout, Locale locale, String extension) {
		return getSiteFileNameBegin(privateLayout) + ImexNormalizer.getDirNameByFriendlyURL(group.getFriendlyURL()) + "_Pages" + extension;
	}
	
	public static String getSiteFileNameBegin(boolean privateLayout) {
		return (privateLayout ? PRIVATE : PUBLIC) + StringPool.UNDERLINE;
	}

}
