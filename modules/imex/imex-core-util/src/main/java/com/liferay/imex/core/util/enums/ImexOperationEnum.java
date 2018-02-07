package com.liferay.imex.core.util.enums;

public enum ImexOperationEnum {
	
	   CREATE ("CREATE"),
	   UPDATE   ("UPDATE");
	

    private final String value;
	
	private ImexOperationEnum(String value_) {
		value = value_;
	}

	public String getValue() {
		return value;
	}

}
