package com.liferay.imex.role.exporter.xml;

import java.util.LinkedList;

public class RolePermissions {
	
	private LinkedList<PortletPermissions> portletPermissionsList = new LinkedList<PortletPermissions>();

	public LinkedList<PortletPermissions> getPortletPermissionsList() {
		return portletPermissionsList;
	}

	public void setPortletPermissionsList(
			LinkedList<PortletPermissions> portletPermissionsList) {
		this.portletPermissionsList = portletPermissionsList;
	}

}
