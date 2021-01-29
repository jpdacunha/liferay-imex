package com.liferay.imex.core.service.exporter.impl;

import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.service.ImexServiceBaseImpl;
import com.liferay.imex.core.service.exporter.model.ExporterProcessIdentifierGenerator;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.UserUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

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
import org.pmw.tinylog.LoggingContext;

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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public String doExportAll() {
		return doExport(StringPool.BLANK);
	}
	
	@Override
	public String doExport(String... names) {		
		return doExport(Arrays.asList(names));		
	}
	
	@Override
	public String doExport(List<String> bundleNames, String profileId) {
		
		//Generate an unique identifier for this export process
		ProcessIdentifierGenerator identifier = new ExporterProcessIdentifierGenerator();
		String identifierStr = identifier.generateUniqueIdentifier();
		
		LoggingContext.put(ImexExecutionReportService.IDENTIFIER_KEY, identifierStr);
		
		reportService.getSeparator(_log);
		if (bundleNames != null && bundleNames.size() > 0) {
			reportService.getStartMessage(_log, "PARTIAL export process for [" + bundleNames.toString() + "]");
		} else {
			reportService.getStartMessage(_log, "ALL export process");
		}
		
		try {
			
			Map<String, ServiceReference<Exporter>> exporters = trackerService.getFilteredExporters(bundleNames);
			
			if (exporters == null || exporters.size() == 0) {
				
				reportService.getMessage(_log, "There is no exporters to execute. Please check : ");
				reportService.getMessage(_log, "- All importers are correctly registered in OSGI container");
				if (bundleNames != null) {
					reportService.getMessage(_log, "- A registered bundle exists for each typed name [" + bundleNames + "]");
				}
				reportService.printKeys(trackerService.getExporters(), _log);
				
			} else {
				
				//Archive actual files before importing
				ImexProperties coreConfig = new ImexProperties();
				configurationService.loadCoreConfiguration(coreConfig);
				reportService.displayConfigurationLoadingInformation(coreConfig, _log);
				imexArchiverService.archiveData(coreConfig.getProperties(), identifier);
				
				File exportDir = initializeExportDirectory();
				
				reportService.getPropertyMessage(_log, "IMEX export path", exportDir.toString());
							
				List<Company> companies = companyLocalService.getCompanies();
				
				for (Company company : companies) {
					
					reportService.getStartMessage(_log, company);
					
					long companyId = company.getCompanyId();
					String companyName = company.getName();
					
					File companyDir = initializeCompanyExportDirectory(exportDir, company);
					
					executeRegisteredExporters(exporters, companyDir, companyId, companyName, profileId);
					
				}
				
			}
			
		} catch (Exception e) {
			reportService.getError(_log, "doExport", e);
		}
		
		reportService.getEndMessage(_log, "export process");
		
		return identifierStr;
		
	}

	@Override
	public String doExport(List<String> bundleNames) {
	
		return doExport(bundleNames, null);

	}
	
	private String getValidProfile(String profileId, Bundle bundle, Exporter exporter, Properties configAsProperties) throws ImexException {
		
		//Manage profile
		String profile = null;
		
		if (exporter.isProfiled()) {
			
			profile =  GetterUtil.getString(configAsProperties.get(ImExCorePropsKeys.DEFAULT_PROFILE_NAME));
			
			if (Validator.isNull(profile)) {
				throw new ImexException("Missing mandatory parameter value [" + ImExCorePropsKeys.DEFAULT_PROFILE_NAME + "]. Please check your configuration file");
			}
			
			if (Validator.isNotNull(profileId)) {
				
				String[] supportedProfiles = CollectionUtil.getArray(configAsProperties.getProperty(ImExCorePropsKeys.MANAGES_PROFILES_LIST));
				
				if (supportedProfiles != null  && supportedProfiles.length > 0) {
					
					if (!Arrays.asList(supportedProfiles).contains(profileId)) {
						throw new ImexException("Unsupported profile [" + profileId + "]. Please check [" + ImExCorePropsKeys.MANAGES_PROFILES_LIST + "] parameter to manage supported profiles list.");
					}
					
					profile = profileId;
					
					
				} else {
					_log.warn("Imex will user default profile because no supported profiles was found. Please check [" + ImExCorePropsKeys.MANAGES_PROFILES_LIST + "] if you want to define a profile list.");
				}
				
			} else {
				_log.debug("[" + bundle.getSymbolicName() + "] is currently using default profile");
			}
			
		} else {
			_log.debug("[" + bundle.getSymbolicName() + "] does not support profile management");
		}
		
		return profile;
		
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
		
		String logsDirPath = configurationService.getImexLogsPath();
		
		File logsDir = new File(logsDirPath);
		logsDir.mkdirs();
		if (!logsDir.exists()) {
			throw new ImexException("Failed to create directory " + logsDir);
		}		
		
		return exportFile;
			
	}
	
	private void executeRegisteredExporters(Map<String, ServiceReference<Exporter>> exporters, File companyDir, long companyId, String companyName, String profileId) throws ImexException {
		
		User user = UserUtil.getDefaultAdmin(companyId);
		
		if (user == null) {
			reportService.getError(_log, "Company [" + companyName + "]", "Missing omni admin user");
			return;
		}
				
		for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
			
			ServiceReference<Exporter> reference = entry.getValue();
			
			Bundle bundle = reference.getBundle();
			
			Exporter exporter = bundle.getBundleContext().getService(reference);
			
			reportService.getStartMessage(_log, exporter.getProcessDescription(), 1);
			
			//Loading configuration for each exporter
			ImexProperties config = new ImexProperties();
			configurationService.loadExporterAndCoreConfiguration(bundle, config);
			reportService.displayConfigurationLoadingInformation(config, _log, bundle);
			
			Properties configAsProperties = config.getProperties();
			if (configAsProperties == null || configAsProperties.size() == 0) {
				reportService.getMessage(_log, bundle, "has no defined configuration.");
			} else {
				reportService.displayProperties(configAsProperties, bundle, _log);
			}
			
			//Manage Root directory
			String exporterRootDirName = exporter.getExporterRootDirectory();
			File exporterRootDir = new File(companyDir, exporterRootDirName);
			boolean success = exporterRootDir.mkdirs();
			if (!success) {
				throw new ImexException("Failed to create directory " + exporterRootDir);
			}
			
			File destDir = exporterRootDir;
			
			//Manage Profile
			String profileDirName = getValidProfile(profileId, bundle, exporter, configAsProperties);
			if (Validator.isNotNull(profileDirName)) {
				
				File profileDir = new File(exporterRootDir, profileDirName);
				success = profileDir.mkdirs();
				if (!success) {
					throw new ImexException("Failed to create directory " + profileDir);
				}
				
				destDir = profileDir;
				
			} else {
				_log.debug("Exporter [" + bundle.getSymbolicName() + "] is not configured to use profiles");
			}
			
			try {
				Company company = companyLocalService.getCompany(companyId);
				exporter.doExport(user, configAsProperties, destDir, companyId, company.getLocale(), true);
			} catch (PortalException e) {
				_log.error(e,e);
			}
												
			reportService.getEndMessage(_log, exporter.getProcessDescription(), 1);
			
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
