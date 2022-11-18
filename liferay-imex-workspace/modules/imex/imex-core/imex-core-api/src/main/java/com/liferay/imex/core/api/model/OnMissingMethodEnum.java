package com.liferay.imex.core.api.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnMissingMethodEnum {

	SKIP ("SKIP"),
	CREATE ("CREATE");
	
	private static Log _log = LogFactoryUtil.getLog(OnMissingMethodEnum.class);
	
	private String value;
	
	private OnMissingMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnMissingMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnMissingMethodEnum b : OnMissingMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}
	
}
