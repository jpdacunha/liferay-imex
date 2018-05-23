package com.liferay.imex.site.exporter;

import com.liferay.imex.core.api.exporter.Exporter;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;

/**
 * @author dev
 */
@Component(
	immediate = true,
	property = {
			"imex.component.execution.priority=10",
			"imex.component.description=Site exporter",
			"service.ranking:Integer=10"
		},
	service = Exporter.class
)
public class SiteExporter implements Exporter {

	@Override
	public void doExport(Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProcessDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}