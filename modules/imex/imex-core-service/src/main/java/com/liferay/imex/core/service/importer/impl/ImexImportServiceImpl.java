package com.liferay.imex.core.service.importer.impl;

import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.identifier.ProcessIdentifier;
import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.service.ImexServiceBaseImpl;
import com.liferay.imex.core.service.importer.model.ImporterProcessIdentifier;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.UserUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
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

@Component(immediate = true, service = ImexImportService.class)
public class ImexImportServiceImpl extends ImexServiceBaseImpl implements ImexImportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexImportServiceImpl.class);
	
	private ImporterTracker trackerService;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexArchiverService imexArchiverService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	public void doImportAll() {
		doImport(StringPool.BLANK);
	}
	
	@Override
	public void doImport(String... names) {		
		doImport(Arrays.asList(names));		
	}

	@Override
	public void doImport(List<String> bundleNames) {
		
		//Generate an unique identifier for this import process
		ProcessIdentifier identifier = new ImporterProcessIdentifier();
		String identifierStr = identifier.getUniqueIdentifier();
				
		reportService.getSeparator(_log);
		if (bundleNames != null && bundleNames.size() > 0) {			
			reportService.getStartMessage(_log, "[" + identifierStr + "] PARTIAL import process for [" + bundleNames.toString() + "]");
		} else {
			reportService.getStartMessage(_log, "[" + identifierStr + "] ALL import process");
		}
		
		try {
			
			Map<String, ServiceReference<Importer>> importers = trackerService.getFilteredImporters(bundleNames);
			
			if (importers == null || importers.size() == 0) {
				
				reportService.getMessage(_log, "There is no importers to execute. Please check : ");
				reportService.getMessage(_log, "- All importers are correctly registered in OSGI container");
				if (bundleNames != null) {
					reportService.getMessage(_log, "- A registered bundle exists for each typed name [" + bundleNames + "]");
				}
				reportService.printKeys(trackerService.getImporters(), _log);
				
			} else {
				
				//Archive actual files before importing
				Properties coreConfig = configurationService.loadCoreConfiguration();
				imexArchiverService.archiveData(coreConfig, identifier);
				
				File importDir = getImportDirectory();
				reportService.getPropertyMessage(_log, "IMEX import path", importDir.toString());
			
				List<Company> companies = companyLocalService.getCompanies();
				
				for (Company company : companies) {
					
					long companyId = company.getCompanyId();
					String companyName = company.getName();
					
					File companyDir = getCompanyImportDirectory(importDir, company);
					
					if (companyDir != null) {
						executeRegisteredImporters(importers, companyDir, companyId, companyName);
					}
					
				}
				
			}
			
		} catch (ImexException | PortalException e) {
			_log.error(e,e);
		}
		
		reportService.getEndMessage(_log, "import process");

	}

	private File getCompanyImportDirectory(File importDir, Company company) {
		
		String webId = company.getWebId();
		File companyDir = new File(importDir, webId);
		
		if (!companyDir.exists()) {
			_log.warn("[" + companyDir.getAbsolutePath() + "] does not exists");
		} 
		return companyDir;
		
	}

	private File getImportDirectory() throws ImexException {
		
		String importFilePath = configurationService.getImexDataPath();
		
		File importFile = new File(importFilePath);
		
		if (!importFile.exists()) {
			throw new ImexException(importFile + " does not exists.");
		} 
		return importFile;
		
	}
	
	private void executeRegisteredImporters(Map<String, ServiceReference<Importer>> importers, File companyDir, long companyId, String companyName) {
				
		User user = UserUtil.getDefaultAdmin(companyId);
		
		if (user == null) {
			reportService.getError(_log, "Company [" + companyName + "]", "Missing omni admin user");
			return;
		}
		
		reportService.getStartMessage(_log, "[" + companyName + "] import process");
		reportService.getMessage(_log, "Using user [" + user.getEmailAddress() + "] as default user");
		
		for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
			
			ServiceReference<Importer> reference = entry.getValue();
			
			Bundle bundle = reference.getBundle();
			
			Importer Importer = bundle.getBundleContext().getService(reference);
			
			reportService.getStartMessage(_log, Importer.getProcessDescription(), 1);
			
			//Loading configuration for each Importer
			Properties config = configurationService.loadImporterAndCoreConfiguration(bundle);
			
			if (config == null) {
				reportService.getMessage(_log, bundle, "has no defined configuration. Aborting execution ...");
			}

			reportService.displayProperties(config, bundle, _log);
			
			try {
				
				Company company = companyLocalService.getCompany(companyId);
				
				ServiceContext serviceContext = new ServiceContext();
				serviceContext.setCompanyId(companyId);
				serviceContext.setPathMain(PortalUtil.getPathMain());
				serviceContext.setUserId(user.getUserId());
				if (user != null) {
					serviceContext.setSignedIn(!user.isDefaultUser());
				}
				
				Importer.doImport(bundle, serviceContext, user, config, companyDir, companyId, company.getLocale(), true);
				
			} catch (PortalException e) {
				_log.error(e,e);
			}
								
			reportService.getEndMessage(_log, Importer.getProcessDescription(), 1);
			
		}
		
		reportService.getEndMessage(_log, "[" + companyName + "] import process");
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(ImporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetImporterTracker(ImporterTracker trackerService) {
		this.trackerService = null;
	}


}
