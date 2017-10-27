package com.liferay.imex.role.exporter.xml;

import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class PortletPermissions {
	
	@Element(required=true)
	private Resource portletResource;
	
	@ElementList(entry="model-resource", inline=true, required=false)
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
