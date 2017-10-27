package com.liferay.imex.role.exporter.xml;

import java.util.LinkedList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class RolePermissions {
	
	@ElementList(entry="portlet-permissions", inline=true, required=false)
	private LinkedList<PortletPermissions> portletPermissionsList = new LinkedList<PortletPermissions>();

	public LinkedList<PortletPermissions> getPortletPermissionsList() {
		return portletPermissionsList;
	}

	public void setPortletPermissionsList(
			LinkedList<PortletPermissions> portletPermissionsList) {
		this.portletPermissionsList = portletPermissionsList;
	}

}
