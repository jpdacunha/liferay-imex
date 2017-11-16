package com.liferay.imex.role.model;

import com.liferay.portal.kernel.model.RoleConstants;

import java.util.HashMap;
import java.util.Map;

public enum RoleType {
	
	COMMUNITY(RoleConstants.TYPE_SITE),
	ORGANIZATION(RoleConstants.TYPE_ORGANIZATION),
	REGULAR(RoleConstants.TYPE_REGULAR),
	TEAM(RoleConstants.TYPE_PROVIDER);
	
	private int intValue;
	
	RoleType(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	private static Map<Integer, RoleType> typesFromIntValue = new HashMap<Integer, RoleType>();
	
	public static RoleType getFromIntValue(int value) {
		if (typesFromIntValue.size() == 0) {
			for (RoleType type : RoleType.values()) {
				typesFromIntValue.put(type.getIntValue(), type);
			}
		}
		
		return typesFromIntValue.get(value);
	}
	
}
