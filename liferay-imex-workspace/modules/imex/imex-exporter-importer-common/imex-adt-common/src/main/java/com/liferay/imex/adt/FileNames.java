package com.liferay.imex.adt;


import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;

import java.util.Locale;

public class FileNames {
	
	public static final String DIR_ADT = "/adt";
	public static final String ADT_FILENAME = "adt";
	
	public static String getAdtFileName(DDMTemplate ddmTemplate, Locale locale, String extension) {
		return getGroupAdtFileName(ddmTemplate, null, locale, extension);
	}
	
	public static String getGroupAdtFileName(DDMTemplate ddmTemplate, Group group, Locale locale, String extension) {
		
		String name = StringPool.BLANK;
		if (group != null) {
			name = GroupUtil.getGroupFriendlyUrlAsName(group) + StringPool.MINUS;
		}
		
		return getAdtFileNameBegin() + name + ddmTemplate.getTemplateKey() + extension;
	}
	
	public static String getAdtFileNameBegin() {
		return FileNames.ADT_FILENAME + StringPool.MINUS;
	}

}
