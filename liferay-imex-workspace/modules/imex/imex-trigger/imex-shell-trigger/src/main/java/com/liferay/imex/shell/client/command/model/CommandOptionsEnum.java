package com.liferay.imex.shell.client.command.model;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


public enum CommandOptionsEnum {
	
	PROFILE ("P"),
	DEBUG ("Debug");
	
	public final static String OPTION_PREFIX = StringPool.MINUS;
	
	private static Log _log = LogFactoryUtil.getLog(CommandOptionsEnum.class);
	
	private String value;
	
	private CommandOptionsEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static CommandOptionsEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (CommandOptionsEnum b : CommandOptionsEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}
	
	public static String getPrefixedParameter(CommandOptionsEnum enumeration) {
		
		if (enumeration != null) {
			return OPTION_PREFIX + enumeration.getValue();
		}
		
		return null;
		
	}
	

}
