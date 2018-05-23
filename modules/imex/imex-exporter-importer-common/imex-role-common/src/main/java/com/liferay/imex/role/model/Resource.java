package com.liferay.imex.role.model;

import java.util.LinkedList;

public class Resource {
	
	private String resourceName;
	
	private LinkedList<Action> actionList = new LinkedList<Action>();
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public LinkedList<Action> getActionList() {
		return actionList;
	}
	public void setActionList(LinkedList<Action> actionList) {
		this.actionList = actionList;
	}	
	
}
