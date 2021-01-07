package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

public class GroupUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(GroupUtil.class);
	
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
		
		if (Validator.isNull(name)) {
			_log.debug("Undefined group name for locale:[" + locale + "], group:[" + group.getFriendlyURL() + "] : using friendly url as group name.");
			return ImexNormalizer.getDirNameByFriendlyURL(group.getFriendlyURL());
		}
		
		return ImexNormalizer.convertToKey(name);
		
	}

}
