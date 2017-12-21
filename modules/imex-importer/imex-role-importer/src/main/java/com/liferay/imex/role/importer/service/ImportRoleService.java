package com.liferay.imex.role.importer.service;

import com.liferay.imex.role.model.ImexRole;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;

public interface ImportRoleService {
	
	public Role importRole(long companyId, User user, ImexRole imexRole) throws PortalException;

}
