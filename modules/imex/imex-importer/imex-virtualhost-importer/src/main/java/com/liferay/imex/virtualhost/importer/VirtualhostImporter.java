package com.liferay.imex.virtualhost.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ProfiledImporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.virtualhost.FileNames;
import com.liferay.imex.virtualhost.importer.configuration.ImExVirtualHostImporterPropsKeys;
import com.liferay.imex.virtualhost.model.ImexVirtualhost;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=20",
			"imex.component.description=Virtualhost importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class VirtualhostImporter implements ProfiledImporter {
	
	private static final String DESCRIPTION = "VIRTUALHOST import";
	
	private static final Log _log = LogFactoryUtil.getLog(VirtualhostImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;

	@Override
	public void doImport(Bundle bundle, ServiceContext rootServiceContext, User exportUser, Properties config, File srcDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "Virtualhost import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExVirtualHostImporterPropsKeys.IMPORT_VIRTUALHOST_ENABLED));
		
		if (enabled) {
		
			try {
				
				File[] files = FileUtil.listFilesByExtension(srcDir, processor.getFileExtension());
				for (File virtualHostFile : files) {
					
					//TODO : JDA continue import process here
					_log.info(">>>>>>>>>>>  " + virtualHostFile.getCanonicalPath());
					
					ImexVirtualhost virtualHostObj = (ImexVirtualhost)processor.read(ImexVirtualhost.class, virtualHostFile);
					
					//this.doImport(serviceContext, companyId, user, config, groupDir, locale, debug);
				}
								
			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "Virtualhost import process");
		
	}
	
	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}
	
	@Override
	public String getRootDirectoryName() {
		return FileNames.DIR_VIRTUALHOST;
	}

	public ImexProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ImexProcessor processor) {
		this.processor = processor;
	}

}
