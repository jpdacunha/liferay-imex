package com.liferay.imex.wcddm.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnExistsTemplateMethodEnum {

	FAIL ("FAIL"),
	UPDATE_EXISTING_TEMPLATE_BY_KEY ("UPDATE_EXISTING_TEMPLATE_BY_KEY");
	
	private static Log _log = LogFactoryUtil.getLog(OnExistsTemplateMethodEnum.class);
	
	private String value;
	
	private OnExistsTemplateMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnExistsTemplateMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnExistsTemplateMethodEnum b : OnExistsTemplateMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}
	
}
