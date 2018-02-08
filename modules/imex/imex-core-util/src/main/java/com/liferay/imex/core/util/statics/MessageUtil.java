package com.liferay.imex.core.util.statics;

import com.liferay.imex.core.util.enums.ImexOperationEnum;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.Locale;

import org.osgi.framework.Bundle;

public class MessageUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(MessageUtil.class);
	
	private final static String PREFIX = "[IMEX] : ";
	
	public static String getStartMessage(Company company) {
		return getStartMessage("for COMPANY : " + getCompanyIdentifier(company), 1);
	}
	

	public static String getStartMessage(String description) {
		return getStartMessage(description, 0);
	}
	
	public static String getStartMessage(Group group, Locale locale) {
		
		return getStartMessage("for GROUP : " + getGroupIdentifier(group, locale), 2);
		
	}
	
	public static String getStartMessage(String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "Starting";
		
		if (Validator.isNotNull(description)){
			rootMessage += StringPool.SPACE + description;
		}
		
		return pad(rootMessage, nbPadLeft);
		
	}
	
	public static String getEmpty(Group group, Locale locale) {
		return getEmpty(getGroupIdentifier(group, locale));
	}
	
	public static String getEmpty(String name) {
		return getMessage("[" + name + "] has no elements", 4);
	}
	
	public static String getDNE(String name) {
		return getMessage("[" + name + "] does not exists", 4);
	}
	
	public static String getDisabled(String name) {
		return getMessage("[" + name + "] is currently [DISABLED]", 4);
	}
	
	public static String getSkipped(String name) {
		return getMessage("[" +name + "] [SKIPPED]", 4);
	}
	
	public static String getUpdate(String name) {
		return getMessage("[" +name + "] [UPDATED]", 4);
	}
	
	public static String getCreate(String name) {
		return getMessage("[" +name + "] [CREATED]", 4);
	}
	
	public static String getOK(String name) {
		return getMessage("[" + name + "] [   OK  ]", 4);
	}
	
	public static String getOK(String key, String name) {
		return getMessage("[" + key + "]=>[" + name + "] [   OK  ]", 4);
	}
	
	public static String getOK(String key, String name, File file, ImexOperationEnum operation) {
		
		String message = "[" + operation.getValue() + "] [" + key + "]=>[" + name + "] [   OK  ]";		
		if (file != null) {
			message += " - (" + file.getAbsolutePath() + ")";
		} else {
			_log.warn("file is null");
		}
		return getMessage(message , 4);
		
	}
	
	public static String getError(String name, String error) {
		return getMessage("[" + name + "] [ ERROR ] : " + error, 4);
	}
	
	public static String getErrorMessage(Exception e) {
		return PREFIX + "An unexpected error occured : " + e.getMessage();
	}
	
	public static String getMessage(Bundle bundle, String description) {
		return PREFIX + "[" + bundle.getSymbolicName() + "] " + description;
	}
	
	public static String getMessage(String description) {
		return getMessage(description, 0);
	}
	
	public static String getMessage(String name, String error) {
		return getMessage("[" + name + "] : " + error, 4);
	}
	
	
	public static String getMessage(String description, int nbPadLeft) {
		
		String rootMessage = StringPool.BLANK;
		
		if (Validator.isNotNull(description)){
			rootMessage = PREFIX + description;
		}
		
		return pad(rootMessage, nbPadLeft);
		
	}
	
	public static String getEndMessage(String description) {
		return getEndMessage(description, 0);
	}
	
	public static String getEndMessage(Group group, Locale locale) {		
		return getEndMessage("process for GROUP : " + getGroupIdentifier(group, locale));		
	}
	
	public static String getEndMessage(String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "End of";
		
		if (Validator.isNotNull(description)){
			rootMessage += StringPool.SPACE + description;
		}
		
		return pad(rootMessage, nbPadLeft);
		
	}
	
	public static String getPropertyMessage(String key, String value) {
		return getPropertyMessage(key, value, 0);
	}
	
	public static String getPropertyMessage(String key, String value, int nbPadLeft) {
		
		String rootMessage = PREFIX;
		
		if (Validator.isNotNull(key) && Validator.isNotNull(value)) {
			
			rootMessage += key + " : " + value;
			
		} else {
			return StringPool.BLANK;
		}
		
		return pad(rootMessage, nbPadLeft);
		
	}
	
	private static String pad(String message, int nbPadLeft) {
		
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
	
	private static String getGroupIdentifier(Group group, Locale locale) {
		
		if (group == null) {
			_log.warn("Group is null");
			return StringPool.BLANK;
		}
		
		String groupName = GroupUtil.getGroupName(group, locale);
		return " groupFriendlyURL:[" + group.getFriendlyURL() + "] groupName:[" + groupName + "]";
	}
	
	private static String getCompanyIdentifier(Company company) {
		
		if (company == null) {
			_log.warn("company is null");
			return StringPool.BLANK;
		}
		
		String name = "";
		try {
			name = company.getName();
		} catch (PortalException e) {
			_log.error(e,e);
		}
		
		return " companyWebId:[" + company.getWebId() + "] companyName:[" + name + "]";
		
	}


}
