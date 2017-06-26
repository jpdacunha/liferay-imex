package com.liferay.product.demo.api.model;

public enum OrderStatusEnum {
	
	NEW("11", "new-11"),

	IN_PROGRESS("12", "in-progress-12"),
	
	SHIPPED("13", "shipped-13"),
	
	CANCELED("14", "canceled-14"),
	
	UNKNOWN("15", "unknowv-15"),
	
	CLOSED("16", "closed-16");

	private String code = null;

	private String key = null;
	
	OrderStatusEnum(String code, String key) {

		this.code = code;
		this.key = key;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
