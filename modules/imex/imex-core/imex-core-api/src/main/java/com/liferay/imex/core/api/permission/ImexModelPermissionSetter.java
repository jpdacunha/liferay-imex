package com.liferay.imex.core.api.permission;

import com.liferay.imex.core.api.permission.model.ModelPermissionBatch;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Resource;

import java.util.List;
import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexModelPermissionSetter {
	
	public int setPermissions(List<ModelPermissionBatch> batchs) throws PortalException, SystemException;
	
	public void setPermissions(Properties props, Bundle bundle, Resource resource) throws SystemException, PortalException;
	
	public void setPermissions(ModelPermissionBatch batch) throws PortalException, SystemException;
	

}
