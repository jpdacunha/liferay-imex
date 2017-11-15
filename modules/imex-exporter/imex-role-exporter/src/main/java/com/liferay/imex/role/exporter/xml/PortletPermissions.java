package com.liferay.imex.role.exporter.xml;

import java.util.LinkedList;

public class PortletPermissions {
	
	private Resource portletResource;
	
	private LinkedList<Resource> modelResourceList = new LinkedList<Resource>();
	
	public Resource getPortletResource() {
		return portletResource;
	}
	public void setPortletResource(Resource portletResource) {
		this.portletResource = portletResource;
	}
	
	public LinkedList<Resource> getModelResourceList() {
		return modelResourceList;
	}
	
	public void setModelResourceList(LinkedList<Resource> modelResources) {
		this.modelResourceList = modelResources;
	}	
}
