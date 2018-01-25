package com.liferay.imex.wcddm.importer.service.impl;

import com.liferay.dynamic.data.mapping.kernel.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.imex.wcddm.importer.service.ImportWcDDMService;
import com.liferay.imex.wcddm.model.ImExStructure;
import com.liferay.imex.wcddm.model.ImExTemplate;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;

import org.osgi.service.component.annotations.Component;

@Component
public class ImportWcDDMServiceImpl implements ImportWcDDMService {

	@Override
	public DDMStructure importStructure(long companyId, User user, Group group, ImExStructure structure)
			throws PortalException {
		//FIXME: JDA code d'import à continuer ici
		return null;
	}

	@Override
	public DDMTemplate importTemplate(long companyId, User user, Group group, ImExTemplate template)
			throws PortalException {
		//FIXME: JDA code d'import à continuer ici
		return null;
	}

}
