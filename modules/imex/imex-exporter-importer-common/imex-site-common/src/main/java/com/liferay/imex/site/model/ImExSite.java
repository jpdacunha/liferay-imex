package com.liferay.imex.site.model;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class ImExSite implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final long DEFAULT_CLASS_PK = -1;

	public static String GLOBAL = "GLOBAL";

	private String className;

	private Map<Locale, String> nameMap = new HashMap<Locale, String>();

	private Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

	private int type;
	
	private String typeLabel;

	private Map<String, String> typeSettings;

	private String friendlyURL;

	private boolean site;

	private boolean active;
	
	private String parentGroupIdFriendlyUrl;

	private int memberShipRestriction;

	private boolean manualMemberShip;

	private boolean inheritContent;

	public ImExSite() {
		
	}
	
	public long getClassPK() {
		//Liferay is able to manage the classPK himself
		return DEFAULT_CLASS_PK;
	}
	
	public ImExSite(Group group, String parentGroupIdFriendlyUrl) {
		
		this.className = group.getClassName();
		this.nameMap = group.getNameMap();
		this.descriptionMap = group.getDescriptionMap();
		this.type = group.getType();
		this.typeSettings = new HashMap<String, String>(group.getTypeSettingsProperties());
		this.friendlyURL = group.getFriendlyURL();
		this.site = group.isSite();
		this.active = group.isActive();
		this.typeLabel = GroupConstants.getTypeLabel(group.getType());
		this.parentGroupIdFriendlyUrl = parentGroupIdFriendlyUrl;
		this.memberShipRestriction = group.getMembershipRestriction();
		this.manualMemberShip = group.getManualMembership();
		this.inheritContent = group.getInheritContent();
	}
	
	public UnicodeProperties getUnicodeProperties() {
		
		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);
		
		Map<String,String> values = this.getTypeSettings();
		
		for (Entry<String,String> entry : values.entrySet()) {
			
			typeSettingsProperties.setProperty(entry.getKey(),GetterUtil.getString(entry.getValue()));
			
		}
		
		
		return typeSettingsProperties;
		
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public Map<String, String> getTypeSettings() {
		return typeSettings;
	}

	public void setTypeSettings(Map<String, String> typeSettings) {
		this.typeSettings = typeSettings;
	}

	public String getFriendlyURL() {
		return friendlyURL;
	}

	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}

	public boolean isSite() {
		return site;
	}

	public void setSite(boolean site) {
		this.site = site;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Map<Locale, String> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<Locale, String> nameMap) {
		this.nameMap = nameMap;
	}

	public Map<Locale, String> getDescriptionMap() {
		return descriptionMap;
	}

	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}

	public String getParentGroupIdFriendlyUrl() {
		return parentGroupIdFriendlyUrl;
	}

	public void setParentGroupIdFriendlyUrl(String parentGroupIdFriendlyUrl) {
		this.parentGroupIdFriendlyUrl = parentGroupIdFriendlyUrl;
	}

	public int getMemberShipRestriction() {
		return memberShipRestriction;
	}

	public void setMemberShipRestriction(int memberShipRestriction) {
		this.memberShipRestriction = memberShipRestriction;
	}

	public boolean isManualMemberShip() {
		return manualMemberShip;
	}

	public void setManualMemberShip(boolean manualMemberShip) {
		this.manualMemberShip = manualMemberShip;
	}

	public boolean isInheritContent() {
		return inheritContent;
	}

	public void setInheritContent(boolean inheritContent) {
		this.inheritContent = inheritContent;
	}

}
