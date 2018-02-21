package com.liferay.imex.core.service.permission.impl;

import com.liferay.imex.core.api.permission.ImexModelPermissionSetter;
import com.liferay.imex.core.service.permission.model.ModelPermissionBatch;
import com.liferay.imex.core.service.permission.model.ModelRolePermissionBatch;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = ImexModelPermissionSetter.class)
public class ImexModelPermissionSetterImpl implements ImexModelPermissionSetter {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexModelPermissionSetterImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.permission.service.ModelPermissionSetter#setPermissions(java.util.List, com.liferay.portal.permission.util.PermissionMessageLogger)
	 */
	public int setPermissions(List<ModelPermissionBatch> batchs) throws PortalException, SystemException {
		
		int nbOk = 0;
		if (batchs != null) {
			for (ModelPermissionBatch batch : batchs) {
				if (batch != null) {
					
					this.setPermissions(batch);
					nbOk++;
					
				}
			}
		}
		return nbOk;
		
	}
	
	/**
	 * Traitement de positionnement des permissions
	 * @param batch
	 * @param logger
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void setPermissions(ModelPermissionBatch batch) throws PortalException, SystemException {
		
		if (ModelPermissionBatch.validate(batch)) {
			
			Resource resource = batch.getResource();
			List<ModelRolePermissionBatch> roleActions = batch.getBatchs();
			
			long companyId = resource.getCompanyId();
			String resourceName = resource.getName();
			String resourcePrimKey = resource.getPrimKey();
			boolean reInitOnSet = batch.reInitOnSet();
			
			_log.info("");
			_log.info("BEGIN : Setting permissions");
			_log.info(" > Selected batch [" + batch.getBatchId() + "] defined for [" + batch.getClassName() + "] ");
			_log.info(" > Permissions reinitialisation [" + reInitOnSet + "] ");
			_log.info(" > Resource [name=" + resourceName + ",id=" + resourcePrimKey + "] ");
			
			//Suppression des permissions sur la resource
			if (reInitOnSet) {				
				resetPermissions(companyId, resource);
			}
			
			//Positionement des permissions d'apres le batch
			for (ModelRolePermissionBatch roleBatch : roleActions) {				
				setPermissionsRole(roleBatch, resource);				
			}
			
			_log.info("END.");
			
		} else {
			_log.warn("Invalid parameter : execution aborted ...");
		}
		
	}

	
	/**
	 * Enffectu le traitement de reinitialisation des permissions
	 * @param companyId
	 * @param resource
	 * @throws PortalException
	 * @throws SystemException
	 */
	private void resetPermissions(long companyId, Resource resource) throws PortalException, SystemException {
		
		if (resource != null) {
			
			String resourceName = resource.getName();
			String resourcePrimKey = resource.getPrimKey();
		
			Role ownerRole = RoleLocalServiceUtil.getRole(companyId, RoleConstants.OWNER);			
			List<String> actions = ResourceActionsUtil.getResourceActions(resourceName);

			String[] arrayActionIds = (String[])actions.toArray(new String[actions.size()]);
			
			ResourcePermissionLocalServiceUtil.deleteResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL, resource.getPrimKey());
			
			ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, resourceName, resource.getScope(), resourcePrimKey, ownerRole.getRoleId(), arrayActionIds);
			
		} else {
			_log.warn("Resource is null : execution aborted ...");
		}
		
	}

	
	/**
	 * Positionne les permissions pour un role
	 * @param roleBatch
	 * @param resource
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void setPermissionsRole(ModelRolePermissionBatch roleBatch, Resource resource) throws PortalException, SystemException {
		
		if (ModelRolePermissionBatch.validate(roleBatch)) {
			
			long companyId = resource.getCompanyId();
			String resourcePrimKey = resource.getPrimKey();
			String resourceName = resource.getName();
			
			//Action convertion
			List<String> actionIds = roleBatch.getActionIds();
			String[] arrayActionIds = actionIds.toArray(new String[actionIds.size()]);
				
			Role role = RoleLocalServiceUtil.getRole(companyId, roleBatch.getRoleName());
			ResourcePermissionLocalServiceUtil.setResourcePermissions(companyId, resourceName, resource.getScope(), resourcePrimKey, role.getRoleId(), arrayActionIds);
			
			_log.info("     [" + roleBatch.getRoleName() + "] -> " + Arrays.toString(arrayActionIds));
						
		} else {
			_log.warn("Invalid parameter : execution aborted ...");
		}
		
	}

}
