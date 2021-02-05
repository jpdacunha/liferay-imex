package com.liferay.imex.expando.exporter;

import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.expando.FileNames;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=20",
			"imex.component.description=Web Content DDM exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)

public class ExpandoExporter implements Exporter{
	
	public static final String DESCRIPTION = "Expando exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(ExpandoExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMTemplateLocalService dDMTemplateLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMStructureLocalService dDMStructureLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ClassNameLocalService classNameLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	public void doExport(User user, Properties config, File destDir, long companyId, Locale locale, List<ExporterRawContent> rawContentToExport, boolean debug) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getRootDirectoryName() {
		return FileNames.DIR_EXPANDO;
	}

}
