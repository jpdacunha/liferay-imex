package com.liferay.imex.wcddm.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum OnExistsStructureMethodEnum {

	FAIL ("FAIL"),
	UPDATE_EXISTING_STRUCTURE_BY_KEY ("UPDATE_EXISTING_STRUCTURE_BY_KEY");
	
	private static Log _log = LogFactoryUtil.getLog(OnExistsStructureMethodEnum.class);
	
	private String value;
	
	private OnExistsStructureMethodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static OnExistsStructureMethodEnum fromValue(String text) {
		
		if (text != null && !text.equals("")) {
			for (OnExistsStructureMethodEnum b : OnExistsStructureMethodEnum.values()) {
				if (text.equalsIgnoreCase(b.value)) {
					return b;
				}
			}
		}
		_log.error("Unable to convert [" + text + "]");
		return null;

	}
	
}
