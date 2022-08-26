package com.liferay.imex.core.api.profile.model;

public class ImexProfile {
	
	public String profileId;
	public String name;
	public String description;
	public ImexProfileCriticityEnum criticity;
	
	public ImexProfile(String profileId) {
		super();
		this.profileId = profileId;
	}
	
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
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
	public ImexProfileCriticityEnum getCriticity() {
		return criticity;
	}
	public void setCriticity(ImexProfileCriticityEnum criticity) {
		this.criticity = criticity;
	}
	
	

}
