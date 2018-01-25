package com.liferay.imex.wcddm.model;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;

import java.io.Serializable;

public class ImExTemplate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2765914408909544775L;

	public ImExTemplate(){}
	
	public ImExTemplate(DDMTemplate jt) {
		
		key = jt.getTemplateKey();
		uuid = jt.getUuid();
		name = jt.getName();
		data = jt.getScript();
		langType = jt.getLanguage();
		templateType = jt.getType();
		
	}
	
	private String uuid;
	
	private String key;

	private String name;
	
	private String data;
	
	private String langType;
	
	private String templateType;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
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
