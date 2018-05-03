package com.liferay.imex.role.importer.service.impl;

import com.liferay.imex.role.importer.service.ImportRolePermissionsService;
import com.liferay.imex.role.model.Action;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.PortletPermissions;
import com.liferay.imex.role.model.Resource;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceBlockLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ResourceBlockLocalService resourceBlockLocalService;
	 
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	
	public void updateRolePermissions(
			long companyId,
			ImexRole imexRole,
			RolePermissions rolePermissions,
			boolean reInit) throws Exception {
		
		if (imexRole != null) {
		
			Role role = roleLocalService.getRoleByUuidAndCompanyId(imexRole.getUuid(), companyId);
			
			if (reInit) {
				reinitAction(role);
			}
			
			for (PortletPermissions portletPermissions : rolePermissions.getPortletPermissionsList()) {
				updateActions(companyId, role, portletPermissions);
			}
			
		} else {
			_log.error("ImexRole is null skipping role import");
		}
		
		
	}
	
	private void reinitAction(Role role) throws SystemException {
		
		long roleId = role.getRoleId();			
	    List<ResourcePermission> liste = resourcePermissionLocalService.getRoleResourcePermissions(roleId);
	    
	    for (ResourcePermission resourcePermission : liste) {
	    	
	    	if (resourcePermission.getScope() != ResourceConstants.SCOPE_INDIVIDUAL) {
	    		resourcePermissionLocalService.deleteResourcePermission(resourcePermission);
	    	}
	    	
	    }
				
	}
	
	private void updateActions(
			long companyId,
			Role role,
			PortletPermissions portletPermissions)
		throws Exception {

		List<Resource> resourceList = new LinkedList<Resource>();
		resourceList.add(portletPermissions.getPortletResource());
		resourceList.addAll(portletPermissions.getModelResourceList());

		for (Resource resource : resourceList) {
		
			List<String> actions = ResourceActionsUtil.getResourceActions(resource.getResourceName());
			actions = ListUtil.sort(actions);
	
			String selResource = resource.getResourceName();
			for (Action action : resource.getActionList()) {
				
				String actionId = action.getActionId();
				int scope = action.getScope().getIntValue();
				Set<String> groupNames = action.getSitesNames();
				String[] groupIds = new String[groupNames.size()];
				int i = 0;
				for (String groupName : groupNames) {
					Group group = groupLocalService.getGroup(companyId, groupName);
					groupIds[i] = Long.toString(group.getGroupId());
					i++;
				}

				updateAction(role, selResource, actionId, scope, groupIds);
				if (_log.isDebugEnabled()) {
					_log.debug("Updating role=[" + role.getName() + "], resource=[" + selResource + "], actionId=[" + actionId + "], scope=[" + scope + "], groupIds=[" + groupIds + "][" + groupIds.length + "]");
				}
				
			}
		}
	}
	
	private void updateAction(Role role, String selResource, String actionId, int scope, String[] groupIds) throws Exception {

		boolean selected = true;

		if (resourceBlockLocalService.isSupported(selResource)) {

			updateActions_6Blocks(role, selResource, actionId, selected, scope,
					groupIds);
		} else {

			updateAction_6(role, selResource, actionId, selected, scope,
					groupIds);

		}

	}
	
	protected void updateAction_6(
			Role role, String selResource, String actionId,
			boolean selected, int scope, String[] groupIds)
		throws Exception {

		long companyId = role.getCompanyId();
		long roleId = role.getRoleId();

		if (selected) {
			if (scope == ResourceConstants.SCOPE_COMPANY) {
				try {
					resourcePermissionLocalService.addResourcePermission(
						companyId, selResource, scope,
						String.valueOf(role.getCompanyId()), roleId, actionId);
				} catch (PrincipalException e) {
					_log.error(e,e);
				}
			}
			else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
				resourcePermissionLocalService.addResourcePermission(
					companyId, selResource,
					ResourceConstants.SCOPE_GROUP_TEMPLATE,
					String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
					roleId, actionId);
			}
			else if (scope == ResourceConstants.SCOPE_GROUP) {
					
				resourcePermissionLocalService.removeResourcePermissions(
					companyId, selResource,
					ResourceConstants.SCOPE_GROUP, roleId, actionId);
				
				for (String curGroupId : groupIds) {
					resourcePermissionLocalService.addResourcePermission(
						companyId, selResource,
						ResourceConstants.SCOPE_GROUP, curGroupId, roleId,
						actionId);
				}
			}
		}
		else {

			// Remove company, group template, and group permissions

			resourcePermissionLocalService.removeResourcePermissions(
				companyId, selResource,
				ResourceConstants.SCOPE_COMPANY, roleId, actionId);

			resourcePermissionLocalService.removeResourcePermissions(
				companyId, selResource,
				ResourceConstants.SCOPE_GROUP_TEMPLATE, roleId, actionId);

			resourcePermissionLocalService.removeResourcePermissions(
				companyId, selResource, ResourceConstants.SCOPE_GROUP,
				roleId, actionId);
		}
	}
	
	protected void updateActions_6Blocks(
			Role role, String selResource, String actionId,
			boolean selected, int scope, String[] groupIds)
		throws Exception {

		long companyId = role.getCompanyId();
		long roleId = role.getRoleId();

		if (selected) {
			if (scope == ResourceConstants.SCOPE_GROUP) {
				resourceBlockLocalService.removeAllGroupScopePermissions(
					 companyId, selResource, roleId, actionId);
				resourceBlockLocalService.removeCompanyScopePermission(
					 companyId, selResource, roleId, actionId);

				for (String groupId : groupIds) {
					resourceBlockLocalService.addGroupScopePermission(
						companyId, GetterUtil.getLong(groupId),
						selResource, roleId, actionId);
				}
			}
			else {
				resourceBlockLocalService.removeAllGroupScopePermissions(
					companyId, selResource, roleId, actionId);
				resourceBlockLocalService.addCompanyScopePermission(
					companyId, selResource, roleId, actionId);
			}
		}
		else {
			resourceBlockLocalService.removeAllGroupScopePermissions(
				companyId, selResource, roleId, actionId);
			resourceBlockLocalService.removeCompanyScopePermission(
				companyId, selResource, roleId, actionId);
		}
	}
	

}
