package com.liferay.imex.role.importer.service.impl;

import com.liferay.imex.role.importer.service.ImportRolePermissionsService;
import com.liferay.imex.role.model.Action;
import com.liferay.imex.role.model.PortletPermissions;
import com.liferay.imex.role.model.Resource;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.imex.role.model.Scope;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortletKeys;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * 
 * @author jpdacunha
 * 
 *
 */
@Component
public class ImportRolePermissionsServiceImpl implements ImportRolePermissionsService {
	
	private static Log _log = LogFactoryUtil.getLog(ImportRolePermissionsServiceImpl.class);
	

}
