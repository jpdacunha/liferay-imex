package com.liferay.imex.adt.model;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;

import java.io.Serializable;

public class ImExAdt implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3376678498797548471L;

	public ImExAdt(){}
	
	public ImExAdt(DDMTemplate jt, String className) throws Exception {
		
		uuid = jt.getUuid();
		name = jt.getName();
		data = jt.getScript();
		langType = jt.getLanguage();
		templateType = jt.getType();
		description = jt.getDescription();
		cacheable = jt.isCacheable();
		key = jt.getTemplateKey();
		this.className = className;
		
		
	}
	
	private String uuid;
	
	private String key;

	private String name;
	
	private String data;
	
	private String langType;
	
	private String templateType;
	
	private String className;
	
	private String description;
	
	private boolean cacheable;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
