package com.liferay.imex.role.exporter.xml;

import java.util.HashSet;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Action {
	
	@Element
	private String actionId;
	@Element
	private Scope scope;
	
	@ElementList(name="sites", entry="site-name", required=false)
	private HashSet<String> sitesNames = new HashSet<String>();
	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public Scope getScope() {
		return scope;
	}
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	public HashSet<String> getSitesNames() {
		return sitesNames;
	}
	public void setSitesNames(HashSet<String> groupNames) {
		this.sitesNames = groupNames;
	}	
}
