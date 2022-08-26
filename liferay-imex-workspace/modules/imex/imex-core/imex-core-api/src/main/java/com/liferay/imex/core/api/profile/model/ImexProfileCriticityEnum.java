package com.liferay.imex.core.api.profile.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

public enum ImexProfileCriticityEnum {

	LOW("low"), 
	NORMAL("normal"), 
	MEDIUM("medium"), 
	HIGH("high");
	
	private static Log _log = LogFactoryUtil.getLog(ImexProfileCriticityEnum.class);
	
	public static ImexProfileCriticityEnum toImexProfileCriticityEnum(String criticity, ImexProfileCriticityEnum defaultValue) {
		
		try {
			
			return ImexProfileCriticityEnum.valueOf(criticity);
			
		} catch (IllegalArgumentException e) {
			_log.warn("Unable to convert [" + criticity + "]");
			return defaultValue;
		}
		
	}

	private final String value;

	private ImexProfileCriticityEnum(String value_) {
			value = value_;
		}

	public String getValue() {
		return value;
	}

}
