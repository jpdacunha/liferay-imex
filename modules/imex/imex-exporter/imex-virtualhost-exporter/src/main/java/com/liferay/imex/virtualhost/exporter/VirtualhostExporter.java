package com.liferay.imex.virtualhost.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.virtualhost.FileNames;
import com.liferay.imex.virtualhost.exporter.configuration.ImExVirtualhostExporterPropsKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=10",
			"imex.component.description=Virtualhost exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class VirtualhostExporter implements Exporter {
	
	public static final String DESCRIPTION = "Virtualhost exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(VirtualhostExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doExport(User user, Properties config, File virtualhostDir, long companyId, Locale locale, boolean debug) {
	
		reportService.getStartMessage(_log, "Virtualhost export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExVirtualhostExporterPropsKeys.EXPORT_VIRTUALHOST_ENABLED));
		
		if (enabled) {
			
			try {
				
				//Company company = companyLocalService.getCompany(companyId);
				
				
				
			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e);
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "Virtualhost export process");
		
	}
	
	/**
	 * Return root directory name
	 *
	 */
	@Override
	public String getExporterRootDirectory() {
		
		return FileNames.DIR_VIRTUALHOST;

	}
	
	@Override
	public boolean isProfiled() {
		return true;
	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

	public ImexProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ImexProcessor processor) {
		this.processor = processor;
	}

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}

}
