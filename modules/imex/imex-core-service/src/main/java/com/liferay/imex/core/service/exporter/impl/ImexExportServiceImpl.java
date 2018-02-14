package com.liferay.imex.core.service.exporter.impl;

import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.core.api.identifier.ProcessIdentifier;
import com.liferay.imex.core.service.ImexServiceBaseImpl;
import com.liferay.imex.core.service.exporter.model.ExporterProcessIdentifier;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.ImexPropsUtil;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.util.Arrays;
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
public class ImexExportServiceImpl extends ImexServiceBaseImpl implements ImexExportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexExportServiceImpl.class);
	
	private ExporterTracker trackerService;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexArchiverService imexArchiverService;

	@Override
	public void doExportAll() {
		doExport(StringPool.BLANK);
	}
	
	@Override
	public void doExport(String... names) {		
		doExport(Arrays.asList(names));		
	}

	@Override
	public void doExport(List<String> bundleNames) {
		
		//Generate an unique identifier for this export process
		ProcessIdentifier identifier = new ExporterProcessIdentifier();
		String identifierStr = identifier.getUniqueIdentifier();
		
		_log.info(MessageUtil.getSeparator());
		if (bundleNames != null && bundleNames.size() > 0) {
			_log.info(MessageUtil.getStartMessage("[" + identifierStr + "] PARTIAL export process for [" + bundleNames.toString() + "]"));
		} else {
			_log.info(MessageUtil.getStartMessage("[" + identifierStr + "] ALL export process"));
		}
		
		try {
			
			Map<String, ServiceReference<Exporter>> exporters = trackerService.getFilteredExporters(bundleNames);
			
			if (exporters == null || exporters.size() == 0) {
				
				_log.error(MessageUtil.getMessage("There is no exporters to execute. Please check : "));
				_log.error(MessageUtil.getMessage("- All importers are correctly registered in OSGI container"));
				if (bundleNames != null) {
					_log.error(MessageUtil.getMessage("- A registered bundle exists for each typed name [" + bundleNames + "]"));
				}
				CollectionUtil.printKeys(trackerService.getExporters(), _log);
				
			} else {
				
				//Archive actual files before importing
				Properties coreConfig = configurationService.loadCoreConfiguration();
				imexArchiverService.archiveData(coreConfig, identifier);
				
				File exportDir = initializeExportDirectory();
				
				_log.info(MessageUtil.getPropertyMessage("IMEX export path", exportDir.toString()));
				
				List<Company> companies = companyLocalService.getCompanies();
				
				for (Company company : companies) {
					
					_log.info(MessageUtil.getStartMessage(company));
					
					long companyId = company.getCompanyId();
					
					File companyDir = initializeCompanyExportDirectory(exportDir, company);
					
					executeRegisteredExporters(exporters, companyDir, companyId);
					
				}
				
			}
			
		} catch (ImexException e) {
			_log.error(e,e);
		}
		
		_log.info(MessageUtil.getEndMessage("[" + identifierStr + "] export process"));

	}

	private File initializeCompanyExportDirectory(File exportDir, Company company) throws ImexException {
		
		String webId = company.getWebId();
		File companyDir = new File(exportDir, webId);
		companyDir.mkdirs();
		if (!companyDir.exists()) {
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
		exportFile.mkdirs();
		if (!exportFile.exists()) {
			throw new ImexException("Failed to create directory " + exportFile);
		}
		
		return exportFile;
			
	}
	
	private void executeRegisteredExporters(Map<String, ServiceReference<Exporter>> exporters, File destDir, long companyId) {
				
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
		
			try {
				Company company = CompanyLocalServiceUtil.getCompany(companyId);
				exporter.doExport(config, destDir, companyId, company.getLocale(), true);
			} catch (PortalException e) {
				_log.error(e,e);
			}
												
			_log.info(MessageUtil.getEndMessage(exporter.getProcessDescription(), 1));
			
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
