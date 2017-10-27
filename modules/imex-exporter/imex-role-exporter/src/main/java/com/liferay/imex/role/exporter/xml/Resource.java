package com.liferay.imex.role.exporter.xml;

import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Resource {
	
	@Element
	private String resourceName;

	@ElementList(entry="action", inline=true, required=false)
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
