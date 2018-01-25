package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.framework.Bundle;

public class MessageUtil {
	
	private final static String PREFIX = "[IMEX] : ";
	
	public static String getStartMessage(String description) {
		return getStartMessage(description, 0);
	}
	
	public static String getStartMessage(String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "Starting";
		
		if (Validator.isNotNull(description)){
			rootMessage += StringPool.SPACE + description;
		}
		
		return pad(rootMessage, nbPadLeft);
		
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


}
