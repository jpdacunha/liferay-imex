package com.liferay.imex.role.model;

import java.io.Serializable;
import java.util.LinkedList;

public class RolePermissions implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1800339614585916828L;
	
	private LinkedList<PortletPermissions> portletPermissionsList = new LinkedList<PortletPermissions>();

	public LinkedList<PortletPermissions> getPortletPermissionsList() {
		return portletPermissionsList;
	}

	public void setPortletPermissionsList(
			LinkedList<PortletPermissions> portletPermissionsList) {
		this.portletPermissionsList = portletPermissionsList;
	}

}
