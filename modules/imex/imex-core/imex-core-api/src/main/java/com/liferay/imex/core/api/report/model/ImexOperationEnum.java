package com.liferay.imex.core.api.report.model;

public enum ImexOperationEnum {
	
	   CREATE ("CREATE"),
	   UPDATE   ("UPDATE"),
	   DELETE   ("DELETE");
	

    private final String value;
	
	private ImexOperationEnum(String value_) {
		value = value_;
	}

	public String getValue() {
		return value;
	}

}
