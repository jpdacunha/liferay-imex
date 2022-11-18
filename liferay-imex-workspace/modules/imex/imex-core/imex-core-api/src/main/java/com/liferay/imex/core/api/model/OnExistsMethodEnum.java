package com.liferay.imex.core.api.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnExistsMethodEnum {
	
	SKIP ("SKIP"),
	RECREATE ("RECREATE"),
	DELETE ("DELETE"),
	PATCH ("PATCH"),
	UPDATE ("UPDATE");
	
	private static Log _log = LogFactoryUtil.getLog(OnExistsMethodEnum.class);
	
	private String value;
	
	private OnExistsMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnExistsMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnExistsMethodEnum b : OnExistsMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}

}
