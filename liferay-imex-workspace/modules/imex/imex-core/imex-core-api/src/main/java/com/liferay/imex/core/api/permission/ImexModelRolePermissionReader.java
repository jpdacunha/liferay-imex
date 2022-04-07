package com.liferay.imex.core.api.permission;

import com.liferay.imex.core.api.permission.model.ModelRolePermissionBatch;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import java.util.List;
import java.util.Properties;

public interface ImexModelRolePermissionReader {
	
	/**
	 * Return all actions defined for a batch
	 * @param batchId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ModelRolePermissionBatch> getRolesBatchs(String batchId, Properties props) throws PortalException, SystemException;
	
	/**
	 * Return true if all permissions need to be reseted before update
	 * @param batchId
	 * @return
	 */
	public boolean isReinitOnset(String batchId, Properties props);

}
