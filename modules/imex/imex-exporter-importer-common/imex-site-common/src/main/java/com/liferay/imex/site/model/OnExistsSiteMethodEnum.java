package com.liferay.imex.site.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnExistsSiteMethodEnum {
	
	SKIP ("SKIP"),
	REPLACE ("REPLACE"),
	UPDATE ("UPDATE");
	
	private static Log _log = LogFactoryUtil.getLog(OnExistsSiteMethodEnum.class);
	
	private String value;
	
	private OnExistsSiteMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnExistsSiteMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnExistsSiteMethodEnum b : OnExistsSiteMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}

}
