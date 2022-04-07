package com.liferay.imex.site.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnMissingSiteMethodEnum {

	SKIP ("SKIP"),
	CREATE ("CREATE");
	
	private static Log _log = LogFactoryUtil.getLog(OnMissingSiteMethodEnum.class);
	
	private String value;
	
	private OnMissingSiteMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnMissingSiteMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnMissingSiteMethodEnum b : OnMissingSiteMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}
	
}
