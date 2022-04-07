package com.liferay.imex.role.exporter.service.impl;

import com.liferay.imex.role.exporter.service.ExportRolePermissionsService;
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
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
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
public class ExportRolePermissionsServiceImpl implements ExportRolePermissionsService {
	
	private static Log _log = LogFactoryUtil.getLog(ExportRolePermissionsServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private PortletLocalService portletLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	
	public RolePermissions getRolePermissions(long companyId, long roleId) throws Exception{
		return getRolePermissions(companyId, roleId, false);
	}
		
	private RolePermissions getRolePermissions(long companyId, long roleId, boolean allActions) throws Exception {
				
		RolePermissions result = new RolePermissions();
		
		List<Portlet> portlets = portletLocalService.getPortlets(companyId, true, false);
		
		List<String> portletNames = new ArrayList<String>(portlets.size() + 1); 
		for (Portlet portlet : portlets) {
			String portletName = portlet.getPortletId();
			portletNames.add(portletName);
		}
		portletNames.add(PortletKeys.PORTAL);
		
		for (String portletName : portletNames) {
			
			PortletPermissions portletPermissions = new PortletPermissions();
			
			List<String> modelResources = ResourceActionsUtil.getPortletModelResources(portletName);
			for (String modelResource : modelResources) {
				
				Resource r = getModelResource(companyId, roleId, modelResource, allActions);
				if (allActions || r.getActionList().size() > 0){
					portletPermissions.getModelResourceList().add(r);
				}
			}
			
			Resource pr = getPortletResource(companyId, roleId, portletName, allActions);
			portletPermissions.setPortletResource(pr);	
			
			if (allActions 
					|| portletPermissions.getModelResourceList().size() > 0
					|| pr.getActionList().size() > 0) {
				result.getPortletPermissionsList().add(portletPermissions);
			}
		}
		
		return result;
	}
	
	private Resource getPortletResource(long companyId, long roleId, String portletResource, boolean allActions) throws PortalException, SystemException {
		List<String> actions = ResourceActionsUtil.getPortletResourceActions(portletResource);
		return getResource(companyId, roleId, portletResource, actions, allActions);
	}
	
	private Resource getModelResource(long companyId, long roleId, String modelResource, boolean allActions) throws PortalException, SystemException {
		List<String> actions = ResourceActionsUtil.getModelResourceActions(modelResource);
		return getResource(companyId, roleId, modelResource, actions, allActions);
		
	}
	
	private Resource getResource(long companyId, long roleId, String resource, List<String> actions, boolean allActions) throws PortalException, SystemException {
	
		Resource r = new Resource();
		r.setResourceName(resource);
		Company company = companyLocalService.getCompany(companyId);
		Role role = roleLocalService.getRole(roleId);
		
		for (String actionId : actions) {			
			
				Action action = new Action();
				action.setActionId(actionId);
				Scope scope = getScope(company, role, resource, actionId);
				action.setScope(scope);							
								
				if (allActions || !scope.equals(Scope.NONE)){
					
					r.getActionList().add(action);
					
					if (scope.equals(Scope.GROUP)) {
						
						LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();
	
						@SuppressWarnings("rawtypes")
						List<Comparable> rolePermissions = new ArrayList<Comparable>();
		
						rolePermissions.add(resource);
						rolePermissions.add(new Integer(ResourceConstants.SCOPE_GROUP));
						rolePermissions.add(actionId);
						rolePermissions.add(new Long(role.getRoleId()));
		
						groupParams.put("rolePermissions", rolePermissions);
							
						List<Group> groups = groupLocalService.search(company.getCompanyId(), null, null, groupParams, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
						for (Group group : groups) {
							if (group.isSite()) {
								action.getSitesNames().add(group.getName());
							} /*else {
								_log.error("Group identified by [" + group.getFriendlyURL() + "] is not a site");
								throw new IllegalStateException();
							}*/
						}
						
					}
				}	
			
		}
		return r;
		
	}
	
	private Scope getScope(Company company, Role role, String curResource, String actionId) throws PortalException, SystemException {
		
		boolean hasCompanyScope = false;
		boolean hasGroupTemplateScope = false;
		boolean hasGroupScope = false;
		
		hasCompanyScope = (role.getType() == RoleConstants.TYPE_REGULAR) && resourcePermissionLocalService.hasScopeResourcePermission(company.getCompanyId(), curResource, ResourceConstants.SCOPE_COMPANY, role.getRoleId(), actionId);
		hasGroupTemplateScope = ((role.getType() == RoleConstants.TYPE_SITE) || (role.getType() == RoleConstants.TYPE_ORGANIZATION)) && resourcePermissionLocalService.hasScopeResourcePermission(company.getCompanyId(), curResource, ResourceConstants.SCOPE_GROUP_TEMPLATE, role.getRoleId(), actionId);
		hasGroupScope = (role.getType() == RoleConstants.TYPE_REGULAR) && resourcePermissionLocalService.hasScopeResourcePermission(company.getCompanyId(), curResource, ResourceConstants.SCOPE_GROUP, role.getRoleId(), actionId);

		Scope result = null;
		if (hasCompanyScope) {
			result = Scope.COMPANY;
		} else if (hasGroupTemplateScope) {
			result = Scope.GROUP_TEMPLATE;
		} else if (hasGroupScope) {
			result = Scope.GROUP;
		} else {
			result = Scope.NONE;
		}
		
		return result;
		
	}

}
