package com.liferay.imex.core.util.statics;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

public class ReportMessageUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(ReportMessageUtil.class);
	
	public static String getGroupIdentifier(Group group, Locale locale) {
		
		if (group == null) {
			_log.warn("Group is null");
			return StringPool.BLANK;
		}
		
		String groupName = GroupUtil.getGroupName(group, locale);
		return " groupFriendlyURL:[" + group.getFriendlyURL() + "] groupName:[" + groupName + "] groupId:[" + group.getGroupId() + "]";
	}
	
	public static String getCompanyIdentifier(Company company) {
		
		if (company == null) {
			_log.warn("company is null");
			return StringPool.BLANK;
		}
		
		String	name = company.getName();

		return " companyWebId:[" + company.getWebId() + "] companyName:[" + name + "]";
		
	}
	
	public static String pad(String message, int nbPadLeft) {
		
		String padValue = StringPool.BLANK;
		
		if (Validator.isNotNull(message)) {
			
			for(int i = 1; i <= nbPadLeft; i++) {
				padValue += StringPool.SPACE;
			}
			
			return padValue + message;
			
		} else {
			return message;
		}
		
	}

}
