package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.model.Group;

import java.util.Locale;

public class GroupUtil {
	
	public static final String GLOBAL = "GLOBAL";
	
	/**
	 * Return the group name
	 * @param group
	 * @param locale
	 * @return
	 */
	public static String getGroupName(Group group, Locale locale) {
		
		String name = group.getName(locale);
		if (group.isCompany()) {
			name = GLOBAL;
		}
		
		return ImexNormalizer.convertToKey(name);
		
	}

}
