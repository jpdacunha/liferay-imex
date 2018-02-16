package com.liferay.imex.adt.model;

import com.liferay.dynamic.data.mapping.kernel.DDMTemplate;

public class ImExAdt {
	
	public ImExAdt(){}
	
	public ImExAdt(DDMTemplate jt, String type) throws Exception {
		
		uuid = jt.getUuid();
		name = jt.getName();
		data = jt.getScript();
		langType = jt.getLanguage();
		templateType = type;
		
	}
	
	private String uuid;

	private String name;
	
	private String data;
	
	private String langType;
	
	private String templateType;
	
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}
}
