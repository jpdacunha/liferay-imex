package com.liferay.imex.wcddm.importer.service;

import com.liferay.dynamic.data.mapping.kernel.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.imex.wcddm.model.ImExStructure;
import com.liferay.imex.wcddm.model.ImExTemplate;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;

public interface ImportWcDDMService {
	
	public DDMStructure importStructure(long companyId, User user, Group group, ImExStructure structure) throws PortalException;
	
	public DDMTemplate importTemplate(long companyId, User user, Group group, ImExTemplate template) throws PortalException;

}
