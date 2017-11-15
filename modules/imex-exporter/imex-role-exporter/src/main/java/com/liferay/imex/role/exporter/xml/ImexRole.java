package com.liferay.imex.role.exporter.xml;

import java.io.Serializable;

public class ImexRole implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8967108660532685046L;
	
	private String uuid;
	private String name;
	private RoleType roleType;
	private String description;
	private String friendlyURL;

	public ImexRole() {
		
	}
	
	public ImexRole(String uuid, String name, int type, String description, String friendlyURL) {
		this.uuid = uuid;
		this.name = name;
		this.roleType = RoleType.getFromIntValue(type);
		this.description = description;
		this.friendlyURL = friendlyURL;
	}
	
	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFriendlyURL() {
		return friendlyURL;
	}

	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

}
