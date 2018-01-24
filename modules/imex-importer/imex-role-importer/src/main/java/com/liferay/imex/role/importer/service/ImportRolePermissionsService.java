package com.liferay.imex.role.importer.service;

import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.RolePermissions;

/**
 * 
 * @author jpdacunha
 * 
 *
 */
public interface ImportRolePermissionsService {
	
	public void updateRolePermissions(
			long companyId,
			ImexRole imexRole,
			RolePermissions rolePermissions,
			boolean reInit) throws Exception;
	
}
