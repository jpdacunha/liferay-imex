package com.liferay.imex.wcddm.importer;

import org.osgi.service.component.annotations.Component;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		immediate = true,
		property = {
			"imex.component.description=Web Content DDM importer",
			"service.ranking:Integer=20"
		},
		service = Importer.class
	)
public class WcDDMImporter implements Importer {
	
	private static final Log _log = LogFactoryUtil.getLog(WcDDMImporter.class);

	@Override
	public void doImport() {
		
		_log.info("Do Wc import ....");
		
	}

}
