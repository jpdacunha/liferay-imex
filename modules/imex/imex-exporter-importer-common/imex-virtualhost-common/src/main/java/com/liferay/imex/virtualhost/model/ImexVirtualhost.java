package com.liferay.imex.virtualhost.model;

import java.io.Serializable;

/**
 * @author dev1
 */
public class ImexVirtualhost implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7834188541223570306L;
	
	public String companyWebId;
	public String groupFriendlyURL;
	public boolean publicVirtualHost;
	public boolean companyVirtualHost;
	public boolean defaultVirtualHost;
	public String hostname;
	
	public String getCompanyWebId() {
		return companyWebId;
	}
	public void setCompanyWebId(String companyWebId) {
		this.companyWebId = companyWebId;
	}
	public String getGroupFriendlyURL() {
		return groupFriendlyURL;
	}
	public void setGroupFriendlyURL(String groupFriendlyURL) {
		this.groupFriendlyURL = groupFriendlyURL;
	}
	public boolean isPublicVirtualHost() {
		return publicVirtualHost;
	}
	public void setPublicVirtualHost(boolean publicVirtualHost) {
		this.publicVirtualHost = publicVirtualHost;
	}
	public boolean isCompanyVirtualHost() {
		return companyVirtualHost;
	}
	public void setCompanyVirtualHost(boolean companyVirtualHost) {
		this.companyVirtualHost = companyVirtualHost;
	}
	public boolean isDefaultVirtualHost() {
		return defaultVirtualHost;
	}
	public void setDefaultVirtualHost(boolean defaultVirtualHost) {
		this.defaultVirtualHost = defaultVirtualHost;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
	
	
}