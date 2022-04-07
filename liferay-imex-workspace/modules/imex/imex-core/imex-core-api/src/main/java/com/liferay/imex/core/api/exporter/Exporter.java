package com.liferay.imex.core.api.exporter;

import com.liferay.imex.core.api.BaseExporterImporter;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.portal.kernel.model.User;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public interface Exporter extends BaseExporterImporter {
	
	public void doExport(User user, Properties config, File destDir, long companyId, Locale locale, List<ExporterRawContent> rawContentToExport, boolean debug);
	
}
