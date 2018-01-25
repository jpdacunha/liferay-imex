package com.liferay.imex.wcddm.model;

import com.liferay.dynamic.data.mapping.model.DDMStructure;

import java.io.Serializable;

public class ImExStructure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5854771479365618395L;

	public ImExStructure(){}
	
	public ImExStructure(DDMStructure js) {

		key = js.getStructureKey();
		uuid = js.getUuid();
		name = js.getName();
		data = js.getDefinition();
		
	}
	
	private String uuid;
	
	private String key;

	private String name;
	
	private String data;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
	
}
