package com.liferay.imex.core.service.permission.impl;

import com.liferay.imex.core.api.permission.ImexModelPermissionSetter;
import com.liferay.imex.core.api.permission.ImexModelRolePermissionReader;
import com.liferay.imex.core.api.permission.model.ModelPermissionBatch;
import com.liferay.imex.core.api.permission.model.ModelRolePermissionBatch;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = ImexModelPermissionSetter.class)
public class ImexModelPermissionSetterImpl implements ImexModelPermissionSetter {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexModelPermissionSetterImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexModelRolePermissionReader reader;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	public void setPermissions(Properties props, Bundle bundle, Resource resource) throws SystemException, PortalException {
		
		String batchId = bundle.getSymbolicName();
		boolean reinitOnset = reader.isReinitOnset(batchId, props);
		List<ModelRolePermissionBatch> roleBatchs = reader.getRolesBatchs(batchId, props);
		ModelPermissionBatch batch = new ModelPermissionBatch(resource, batchId, reinitOnset, roleBatchs);
		
		setPermissions(batch);
		
	}
	
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
			
			reportService.getStartMessage(_log, "Setting permissions");
			
			Resource resource = batch.getResource();
			List<ModelRolePermissionBatch> roleActions = batch.getBatchs();
			
			long companyId = resource.getCompanyId();
			String resourceName = resource.getName();
			String resourcePrimKey = resource.getPrimKey();
			boolean reInitOnSet = batch.isReInitOnSet();
	
			reportService.getPropertyMessage(_log, "Selected batch", batch.getBatchId());
			reportService.getPropertyMessage(_log, "Permissions reinitialisation", reInitOnSet + "");
			reportService.getPropertyMessage(_log, "Resource Name", resourceName);
			reportService.getPropertyMessage(_log, "Resource Primary Key", resourcePrimKey);
			
			if (roleActions != null && roleActions.size() > 0) {
				//Deleting previous setted permissions
				if (reInitOnSet) {				
					resetPermissions(companyId, resource);
				}
				
				//Setting permissions on resource
				for (ModelRolePermissionBatch roleBatch : roleActions) {				
					setPermissionsRole(roleBatch, resource);				
				}
			} else {
				reportService.getMessage(_log, "No permissions to set");
			}
			
			reportService.getEndMessage(_log, "Setting permissions");
			
		} else {
			reportService.getError(_log, "Invalid parameter", "Unable to execute invalid batch");
		}
		
	}

	
	/**
	 * Réalise le traitement de reinitialisation des permissions
	 * @param companyId
	 * @param resource
	 * @throws PortalException
	 * @throws SystemException
	 */
	private void resetPermissions(long companyId, Resource resource) throws PortalException, SystemException {
		
		if (resource != null) {
			
			String resourceName = resource.getName();
			String resourcePrimKey = resource.getPrimKey();
		
			Role ownerRole = roleLocalService.getRole(companyId, RoleConstants.OWNER);			
			List<String> actions = ResourceActionsUtil.getResourceActions(resourceName);

			String[] arrayActionIds = (String[])actions.toArray(new String[actions.size()]);
			
			resourcePermissionLocalService.deleteResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL, resource.getPrimKey());
			
			resourcePermissionLocalService.setResourcePermissions(companyId, resourceName, resource.getScope(), resourcePrimKey, ownerRole.getRoleId(), arrayActionIds);
			
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
	private void setPermissionsRole(ModelRolePermissionBatch roleBatch, Resource resource) throws PortalException, SystemException {
		
		if (ModelRolePermissionBatch.validate(roleBatch)) {
			
			long companyId = resource.getCompanyId();
			String resourcePrimKey = resource.getPrimKey();
			String resourceName = resource.getName();
			
			//Action convertion
			List<String> actionIds = roleBatch.getActionIds();
			String[] arrayActionIds = actionIds.toArray(new String[actionIds.size()]);
			
			if (arrayActionIds != null && arrayActionIds.length > 0) {
				
				Role role = roleLocalService.getRole(companyId, roleBatch.getRoleName());
				resourcePermissionLocalService.setResourcePermissions(companyId, resourceName, resource.getScope(), resourcePrimKey, role.getRoleId(), arrayActionIds);
				_log.debug("     [" + roleBatch.getRoleName() + "] -> " + Arrays.toString(arrayActionIds));
			
			} else {
				
				_log.debug("     [" + roleBatch.getRoleName() + "] as no defined permissions");
				
			}
					
		} else {
			_log.warn("Invalid parameter : execution aborted ...");
		}
		
	}

}
