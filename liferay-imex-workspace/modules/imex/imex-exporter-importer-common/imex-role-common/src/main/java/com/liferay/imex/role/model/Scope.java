package com.liferay.imex.role.model;

import com.liferay.portal.kernel.model.ResourceConstants;

import java.util.HashMap;
import java.util.Map;

public enum Scope {
	
	NONE(ResourceConstants.SCOPE_COMPANY-1),
	COMPANY(ResourceConstants.SCOPE_COMPANY),
	GROUP(ResourceConstants.SCOPE_GROUP),
	GROUP_TEMPLATE(ResourceConstants.SCOPE_GROUP_TEMPLATE);
	
	private int intValue;
	
	Scope(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	private static Map<Integer, Scope> scopesFromIntValue = new HashMap<Integer, Scope>();
	
	public static Scope getFromIntValue(int value) {
		if (scopesFromIntValue.size() == 0) {
			for (Scope scope : Scope.values()) {
				scopesFromIntValue.put(scope.getIntValue(), scope);
			}
		}
		
		return scopesFromIntValue.get(value);
	}
	
}
