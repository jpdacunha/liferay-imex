package com.liferay.imex.role.importer;

import org.osgi.service.component.annotations.Component;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		immediate = true,
		property = {
			"imex.component.description=Roles importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class RoleImporter implements Importer {
	
	private static final Log _log = LogFactoryUtil.getLog(RoleImporter.class);

	@Override
	public void doImport() {
		
		_log.info("Do role import ....");
		
	}

}
