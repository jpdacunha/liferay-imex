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
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * 
 * @author jpdacunha
 * 
 *
 */
@Component
public class ImportRolePermissionsServiceImpl implements ImportRolePermissionsService {
	
	private static Log _log = LogFactoryUtil.getLog(ImportRolePermissionsServiceImpl.class);
	
	public void updateRolePermissions(
			long companyId,
			ImexRole imexRole,
			RolePermissions rolePermissions,
			boolean reInit) throws Exception {
		
		if (imexRole != null) {
		
			Role role = RoleLocalServiceUtil.getRoleByUuidAndCompanyId(imexRole.getUuid(), companyId);
			
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
	    List<ResourcePermission> liste = ResourcePermissionLocalServiceUtil.getRoleResourcePermissions(roleId);
	    
	    for (ResourcePermission resourcePermission : liste) {
	    	
	    	if (resourcePermission.getScope() != ResourceConstants.SCOPE_INDIVIDUAL) {
	    		ResourcePermissionLocalServiceUtil.deleteResourcePermission(resourcePermission);
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
					Group group = GroupLocalServiceUtil.getGroup(companyId, groupName);
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

		if (ResourceBlockLocalServiceUtil.isSupported(selResource)) {

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
					ResourcePermissionLocalServiceUtil.addResourcePermission(
						companyId, selResource, scope,
						String.valueOf(role.getCompanyId()), roleId, actionId);
				} catch (PrincipalException e) {
					_log.error(e,e);
				}
			}
			else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
				ResourcePermissionLocalServiceUtil.addResourcePermission(
					companyId, selResource,
					ResourceConstants.SCOPE_GROUP_TEMPLATE,
					String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
					roleId, actionId);
			}
			else if (scope == ResourceConstants.SCOPE_GROUP) {
					
				ResourcePermissionLocalServiceUtil.removeResourcePermissions(
					companyId, selResource,
					ResourceConstants.SCOPE_GROUP, roleId, actionId);
				
				for (String curGroupId : groupIds) {
					ResourcePermissionLocalServiceUtil.addResourcePermission(
						companyId, selResource,
						ResourceConstants.SCOPE_GROUP, curGroupId, roleId,
						actionId);
				}
			}
		}
		else {

			// Remove company, group template, and group permissions

			ResourcePermissionLocalServiceUtil.removeResourcePermissions(
				companyId, selResource,
				ResourceConstants.SCOPE_COMPANY, roleId, actionId);

			ResourcePermissionLocalServiceUtil.removeResourcePermissions(
				companyId, selResource,
				ResourceConstants.SCOPE_GROUP_TEMPLATE, roleId, actionId);

			ResourcePermissionLocalServiceUtil.removeResourcePermissions(
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
				ResourceBlockLocalServiceUtil.removeAllGroupScopePermissions(
					 companyId, selResource, roleId, actionId);
				ResourceBlockLocalServiceUtil.removeCompanyScopePermission(
					 companyId, selResource, roleId, actionId);

				for (String groupId : groupIds) {
					ResourceBlockLocalServiceUtil.addGroupScopePermission(
						companyId, GetterUtil.getLong(groupId),
						selResource, roleId, actionId);
				}
			}
			else {
				ResourceBlockLocalServiceUtil.removeAllGroupScopePermissions(
					companyId, selResource, roleId, actionId);
				ResourceBlockLocalServiceUtil.addCompanyScopePermission(
					companyId, selResource, roleId, actionId);
			}
		}
		else {
			ResourceBlockLocalServiceUtil.removeAllGroupScopePermissions(
				companyId, selResource, roleId, actionId);
			ResourceBlockLocalServiceUtil.removeCompanyScopePermission(
				companyId, selResource, roleId, actionId);
		}
	}
	

}
