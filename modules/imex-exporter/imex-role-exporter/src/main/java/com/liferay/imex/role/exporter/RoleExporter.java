package com.liferay.imex.role.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
			"imex.component.description=Role exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class RoleExporter implements Exporter {
	
	private static final Log _log = LogFactoryUtil.getLog(RoleExporter.class);
	
	public static final String DIR_ROLE = "/role";

	@Override
	public void doExport() {
		
		_log.info("Do Role export ....");
		
	}

	@Override
	public String getDirectoryName() {
		return DIR_ROLE;
	}

	@Override
	public String getProcessDescription() {
		return "Liferay Role Export Process";
	}

}
