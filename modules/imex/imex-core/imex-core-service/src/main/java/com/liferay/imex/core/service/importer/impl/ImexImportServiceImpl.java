package com.liferay.imex.core.service.importer.impl;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.service.ImexServiceBaseImpl;
import com.liferay.imex.core.service.importer.model.ImporterProcessIdentifier;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.UserUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.pmw.tinylog.LoggingContext;

@Component(immediate = true, service = ImexImportService.class)
public class ImexImportServiceImpl extends ImexServiceBaseImpl implements ImexImportService {
	
	public final static String IMEX_IMPORT_SERVICE_ERROR = "Importer core service error";
	
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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexCoreService imexCoreService;
	
	@Override
	public String doImportAll() {
		return doImport(StringPool.BLANK);
	}
	
	@Override
	public String doImport(String... names) {		
		return doImport(Arrays.asList(names));		
	}
	
	@Activate
	public void activate() {
		imexCoreService.initializeLock();
		_log.info("Lock succesfully initialized by IMEX core");
	}
	
	@Override
	public String doImport(List<String> bundleNames, String profileId) {
		return doImport(bundleNames, profileId, false);
	}

	@Override
	public String doImport(List<String> bundleNames, String profileId, boolean debug) { 
		
		// Generate an unique identifier for this import process
		ProcessIdentifierGenerator identifier = new ImporterProcessIdentifier();
		String identifierStr = identifier.getOrGenerateUniqueIdentifier();
		LoggingContext.put(ImexExecutionReportService.IDENTIFIER_KEY, identifierStr);
		
		try {
			
			if (imexCoreService.tryLock()) {
				
				reportService.getSeparator(_log);
				if (bundleNames != null && bundleNames.size() > 0) {			
					reportService.getStartMessage(_log, "PARTIAL import process for [" + bundleNames.toString() + "]");
				} else {
					reportService.getStartMessage(_log, "ALL import process");
				}
				
				try {
					
					if (debug) {
						reportService.getMessage(_log, "Running DEBUG mode ...");
					}
					
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
						ImexProperties coreConfig = new ImexProperties();
						configurationService.loadCoreConfiguration(coreConfig);
						reportService.displayConfigurationLoadingInformation(coreConfig, _log);
						imexArchiverService.archiveDataDirectory(coreConfig.getProperties(), identifier);
						
						File importDir = getImportDirectory();
						reportService.getPropertyMessage(_log, "IMEX import path", importDir.toString());
					
						List<Company> companies = companyLocalService.getCompanies();
						
						//For each Liferay company
						for (Company company : companies) {
							
							long companyId = company.getCompanyId();
							String companyName = company.getName();
							
							File companyDir = getCompanyImportDirectory(importDir, company);
							
							if (companyDir != null) {
								executeRegisteredImporters(importers, companyDir, companyId, companyName, profileId, debug);
							}
							
						}
						
					}
					
				} catch (Exception e) {
					reportService.getError(_log, "doImport", e);
				}
				
				reportService.getEndMessage(_log, "import process");
				
				
		
			} else {
				
				reportService.getMessage(_log, "##");
				reportService.getMessage(_log, "## " + ImexCoreService.LOCKED_MESSAGE);
				reportService.getMessage(_log, "##");
				
			}
				
		} finally {
			imexCoreService.releaseLock();
		}
		
		return identifierStr;
		
	}
	
	@Override
	public String doImport(List<String> bundleNames) {
	
		return doImport(bundleNames, null);

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
	
	private void executeRegisteredImporters(Map<String, ServiceReference<Importer>> importers, File companyDir, long companyId, String companyName, String profileId, boolean debug) throws ImexException {
				
		User user = UserUtil.getDefaultAdmin(companyId);
		
		if (user == null) {
			reportService.getError(_log, "Company [" + companyName + "]", "Missing omni admin user");
			return;
		}
		
		reportService.getStartMessage(_log, "company : [" + companyName + "] import process");
		reportService.getMessage(_log, "Using user [" + user.getEmailAddress() + "] as default user");
		
		for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
			
			ServiceReference<Importer> reference = entry.getValue();
			
			Bundle bundle = reference.getBundle();
			
			Importer importer = bundle.getBundleContext().getService(reference);
			
			reportService.getStartMessage(_log, importer.getProcessDescription(), 1);
			
			//Loading configuration for each Importer
			ImexProperties config = new ImexProperties();
			configurationService.loadImporterAndCoreConfiguration(bundle, config);
			reportService.displayConfigurationLoadingInformation(config, _log, bundle);
			
			Properties configAsProperties = config.getProperties();
			if (configAsProperties == null || configAsProperties.size() == 0) {
				reportService.getMessage(_log, bundle, "has no defined configuration.");
			} else {
				reportService.displayProperties(configAsProperties, bundle, _log);
			}
			
			try {
				
				//Manage Root directory
				File destDir = getRootDirectory(companyDir, importer);
				
				if (destDir != null) {
				
					//Manage Profile
					String profileDirName = getValidProfile(profileId, bundle, importer, configAsProperties);
					if (Validator.isNotNull(profileDirName)) {
						
						File profileDir = new File(destDir, profileDirName);			
						destDir = profileDir;
						
					} else {
						_log.debug("Importer [" + bundle.getSymbolicName() + "] is not configured to use profiles");
					}
					
					Company company = companyLocalService.getCompany(companyId);
					
					ServiceContext serviceContext = new ServiceContext();
					serviceContext.setCompanyId(companyId);
					serviceContext.setPathMain(PortalUtil.getPathMain());
					serviceContext.setUserId(user.getUserId());
					if (user != null) {
						serviceContext.setSignedIn(!user.isDefaultUser());
					}
						
					//Managing locale
					Locale locale = company.getLocale();
					//This is a workaround because Liferay is not able to manage default locale correctly in batch mode
					LocaleThreadLocal.setDefaultLocale(locale);
					
					if (locale == null) {
						reportService.getError(_log, IMEX_IMPORT_SERVICE_ERROR, "company default locale is null");
					} else {
						reportService.getMessage(_log, "Using [" + locale + "] as default locale");
					}
					
					importer.doImport(bundle, serviceContext, user, configAsProperties, destDir, companyId, locale, debug);
					
				} else {
					reportService.getSkipped(_log, "[" + bundle.getSymbolicName() + "]");
					
				}
				
			} catch (PortalException e) {
				_log.error(e,e);
			}
								
			reportService.getEndMessage(_log, importer.getProcessDescription(), 1);
			
		}
		
		reportService.getEndMessage(_log, "[" + companyName + "] import process");
		
	}
	
	private File getRootDirectory(File companyDir, Importer importer) throws ImexException {
		
		String exporterRootDirName = importer.getRootDirectoryName();
		
		if (Validator.isNull(exporterRootDirName)) {
			throw new ImexException("Missing required parameter root exporter name for [" + importer.getClass().getName() + "]");
		}
		
		File dir = new File(companyDir, exporterRootDirName);
		
		if (!dir.exists() || !dir.isDirectory()) {
			dir = null;
			try {
				reportService.getDNE(_log, "["  + companyDir.getCanonicalPath() + "/" + exporterRootDirName + "]");
			} catch(IOException e) {
				_log.error(e,e);
			}
				
		}
		
		return dir;
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(ImporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetImporterTracker(ImporterTracker trackerService) {
		this.trackerService = null;
	}

	public ImporterTracker getTrackerService() {
		return trackerService;
	}

	public void setTrackerService(ImporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	public CompanyLocalService getCompanyLocalService() {
		return companyLocalService;
	}

	public void setCompanyLocalService(CompanyLocalService companyLocalService) {
		this.companyLocalService = companyLocalService;
	}

	public ImexConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ImexConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public ImexArchiverService getImexArchiverService() {
		return imexArchiverService;
	}

	public void setImexArchiverService(ImexArchiverService imexArchiverService) {
		this.imexArchiverService = imexArchiverService;
	}

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}

	public ImexCoreService getImexCoreService() {
		return imexCoreService;
	}

	public void setImexCoreService(ImexCoreService imexCoreService) {
		this.imexCoreService = imexCoreService;
	}


}
