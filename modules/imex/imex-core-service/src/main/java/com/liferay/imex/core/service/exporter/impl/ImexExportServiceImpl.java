package com.liferay.imex.core.service.exporter.impl;

import com.liferay.imex.core.api.ImexConfigurationService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.core.util.configuration.ImexPropsUtil;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate = true, service = ImexExportService.class)
public class ImexExportServiceImpl implements ImexExportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexExportServiceImpl.class);
	
	private ExporterTracker trackerService;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;

	@Override
	public void doExport() {
		
		_log.info(MessageUtil.getStartMessage("export process"));
		
		
		try {
			File exportDir = initializeExportDirectory();
			
			_log.info(MessageUtil.getPropertyMessage("IMEX export path", exportDir.toString()));
			
			Map<String, ServiceReference<Exporter>> exporters = trackerService.getExporters();
			
			List<Company> companies = companyLocalService.getCompanies();
			
			for (Company company : companies) {
				
				long companyId = company.getCompanyId();
				
				File companyDir = initializeCompanyExportDirectory(exportDir, company);
				
				executeRegisteredExporters(exporters, companyDir, companyId);
				
			}
			
		} catch (ImexException e) {
			_log.error(e,e);
		}
		
		_log.info(MessageUtil.getEndMessage("export process"));

	}

	private File initializeCompanyExportDirectory(File exportDir, Company company) throws ImexException {
		
		String webId = company.getWebId();
		File companyDir = new File(exportDir, webId);
		boolean success = companyDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + companyDir);
		}
		
		return companyDir;
		
	}
	
	private File initializeExportDirectory() throws ImexException {
				
		String exportFilePath = configurationService.getImexDataPath();
		
		File exportFile = new File(exportFilePath);
		
		if (exportFile.exists()) {
			FileUtil.deltree(exportFile);
		}
		boolean success = exportFile.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + exportFile);
		}
		
		return exportFile;
			
	}
	
	private void executeRegisteredExporters(Map<String, ServiceReference<Exporter>> exporters, File destDir, long companyId) {
		
		if (exporters != null && exporters.size() > 0) {
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				Exporter exporter = bundle.getBundleContext().getService(reference);
				
				_log.info(MessageUtil.getStartMessage(exporter.getProcessDescription(), 1));
				
				//Loading configuration for each exporter
				Properties config = configurationService.loadExporterConfiguration(bundle);
				
				if (config == null) {
					_log.warn(MessageUtil.getMessage(bundle, "has no defined configuration. Aborting execution ..."));
				}
				
				ImexPropsUtil.displayProperties(config, bundle);
			
				//FIXME : manage debug param
				exporter.doExport(config, destDir, companyId, true);
									
				_log.info(MessageUtil.getEndMessage(exporter.getProcessDescription(), 1));
				
			}
			
		} else {
			_log.info(MessageUtil.getMessage("No registered exporters"));
		}
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.trackerService = null;
	}

}
