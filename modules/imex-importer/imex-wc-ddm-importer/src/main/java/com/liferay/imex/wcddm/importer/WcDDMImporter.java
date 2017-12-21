package com.liferay.imex.wcddm.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;

import java.io.File;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;

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
	public void doImport(User user, Properties config, File roleDir, long companyId, boolean debug) {
		
		_log.info("Do Wc import ....");
		
	}

	@Override
	public String getProcessDescription() {
		return "Web Content DDM import";
	}

}
