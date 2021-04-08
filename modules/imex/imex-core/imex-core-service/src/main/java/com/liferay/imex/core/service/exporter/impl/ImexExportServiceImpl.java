package com.liferay.imex.core.service.exporter.impl;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.service.ImexServiceBaseImpl;
import com.liferay.imex.core.service.exporter.model.ExporterProcessIdentifierGenerator;
import com.liferay.imex.core.service.exporter.model.RawExporterProcessIdentifierGeneratorWrapper;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.UserUtil;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
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
	
	private List<ExporterRawContent> rawExportContentList = Collections.synchronizedList(new ArrayList<ExporterRawContent>());
	
	private ExporterTracker trackerService;

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
	public String doExportAll() {
		return doExport(new ArrayList<>());
	}
	
	@Override
	public String doExport(String... names) {		
		return doExport(Arrays.asList(names));		
	}
	
	@Override
	public String doExport(List<String> bundleNames, String profileId) {
		return doExport(bundleNames, profileId, false);
	}
	
	@Override
	public String doExport(List<String> bundleNames, String profileId, boolean debug) {
		
		//Generate an unique identifier for this export process
		ProcessIdentifierGenerator identifier = new ExporterProcessIdentifierGenerator();
		String identifierStr = identifier.getOrGenerateUniqueIdentifier();
		LoggingContext.put(ImexExecutionReportService.IDENTIFIER_KEY, identifierStr);
		
		try {
			
			if (imexCoreService.tryLock()) {
			
				reportService.getSeparator(_log);
				if (bundleNames != null && bundleNames.size() > 0) {
					reportService.getStartMessage(_log, "PARTIAL export process for [" + bundleNames.toString() + "]");
				} else {
					reportService.getStartMessage(_log, "ALL export process");
				}
				
				try {
					
					if (debug) {
						reportService.getMessage(_log, "Running DEBUG mode ...");
					}
					
					Map<String, ServiceReference<Exporter>> exporters = trackerService.getFilteredExporters(bundleNames);
					
					if (exporters == null || exporters.size() == 0) {
						
						reportService.getMessage(_log, "There is no exporters to execute. Please check : ");
						reportService.getMessage(_log, "- All importers are correctly registered in OSGI container");
						if (bundleNames != null) {
							reportService.getMessage(_log, "- A registered bundle exists for each typed name [" + bundleNames + "]");
						}
						reportService.printKeys(trackerService.getExporters(), _log);
						
					} else {
						
						//Reading configuration
						ImexProperties coreConfig = new ImexProperties();
						configurationService.loadCoreConfiguration(coreConfig);
						reportService.displayConfigurationLoadingInformation(coreConfig, _log);
						
						//Archive actual files before importing
						imexArchiverService.archiveDataDirectory(coreConfig.getProperties(), identifier);
						
						File exportDir = initializeExportDirectory();
						
						reportService.getPropertyMessage(_log, "IMEX export path", exportDir.toString());
									
						List<Company> companies = companyLocalService.getCompanies();
						
						//For each Liferay company
						for (Company company : companies) {
							
							reportService.getStartMessage(_log, company);
							
							long companyId = company.getCompanyId();
							String companyName = company.getName();
							
							File companyDir = initializeCompanyExportDirectory(exportDir, company);
							
							executeRegisteredExporters(exporters, companyDir, companyId, companyName, profileId, debug);
							
						}
						
						reportService.getSeparator(_log);
						
						//Executing raw export
						executeRawExport((ExporterProcessIdentifierGenerator)identifier, debug);
						
					}
					
				} catch (Exception e) {
					reportService.getError(_log, "doExport", e);
				}
				
				reportService.getEndMessage(_log, "export process");
				
				
			
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
	public String doExport(List<String> bundleNames) {
	
		return doExport(bundleNames, null);

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
	
	private File initializeRawExportDirectory() throws ImexException {
		
		String exportRawDataFilePath = configurationService.getImexRawDataPath();
		
		File exportRawDataFile = new File(exportRawDataFilePath);
		
		if (!exportRawDataFile.exists()) {
			exportRawDataFile.mkdirs();
		}
		
		if (!exportRawDataFile.exists()) {
			throw new ImexException("Failed to create directory " + exportRawDataFile);
		}	
		
		return exportRawDataFile;
			
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
	
	private void executeRegisteredExporters(Map<String, ServiceReference<Exporter>> exporters, File companyDir, long companyId, String companyName, String profileId, boolean debug) throws ImexException {
		
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
			String exporterRootDirName = exporter.getRootDirectoryName();
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
				
				//Trigger Exporter specific code
				exporter.doExport(user, configAsProperties, destDir, companyId, company.getLocale(), this.rawExportContentList, debug);
				
					
			} catch (PortalException e) {
				_log.error(e,e);
			}
						
			reportService.getEndMessage(_log, exporter.getProcessDescription(), 1);
			
		}

	}
	
	private void executeRawExport(ExporterProcessIdentifierGenerator rootIdentifier, boolean debug) throws ImexException, IOException {
		
		ImexProperties config = new ImexProperties();
		configurationService.loadCoreConfiguration(config);
		Properties coreConfigAsProperties = config.getProperties();
		
		boolean rawExportEnabled =  GetterUtil.getBoolean(coreConfigAsProperties.get(ImExCorePropsKeys.RAW_CONTENT_EXPORTER_ENABLED));
		
		if (rawExportEnabled) {
			
			ProcessIdentifierGenerator identifier = new RawExporterProcessIdentifierGeneratorWrapper(rootIdentifier);
			
			String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(4);
			File tempDir = Files.createTempDirectory(randomAlphanumeric).toFile();
			
			if (!tempDir.exists()) {
				throw new ImexException("Failed to create directory " + tempDir);
			}	
			
			File exportDir = initializeRawExportDirectory();
			
			reportService.getStartMessage(_log, "Raw export process (human readable format)", 1);
			
			//Since this a synchronized list
			synchronized (rawExportContentList) {
				for (ExporterRawContent content : rawExportContentList) {
				
					writeRawContentInDir(tempDir, content, debug);
					
				}
			}
			
			imexArchiverService.archiveAndClean(coreConfigAsProperties, tempDir, exportDir, identifier);
			
			FileUtil.deltree(tempDir);
			
			rawExportContentList.clear();
			
			reportService.getEndMessage(_log, "Raw export process", 1);
			
		} else {
			
			if (rawExportContentList.size() > 0) {
				reportService.getDisabled(_log, "raw content export");
				reportService.getMessage(_log, "See [" + ImExCorePropsKeys.RAW_CONTENT_EXPORTER_ENABLED + "] to enable this feature", 4);
			}
		
		}
		
	}

	private void writeRawContentInDir(File tempDir, ExporterRawContent content, boolean debug) throws IOException {
		
		FileWriter fileWriter = null;
		try {
			File currentFile = new File(tempDir, content.getFileName());
			if (currentFile.exists()) {
				reportService.getError(_log, "Ambiguous file name detected", "[" + currentFile.getAbsolutePath() + "] already exists");
			}
			fileWriter = new FileWriter(currentFile);
			fileWriter.write(content.getFileContent());
			if (debug) {
				reportService.getOK(_log, currentFile.getAbsolutePath());
			}
		} finally {
			IOUtils.closeQuietly(fileWriter);
		}
		
	}

	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.trackerService = null;
	}

	public List<ExporterRawContent> getRawExportContentList() {
		return rawExportContentList;
	}

	public void setRawExportContentList(List<ExporterRawContent> rawExportContentList) {
		this.rawExportContentList = rawExportContentList;
	}

	public ExporterTracker getTrackerService() {
		return trackerService;
	}

	public void setTrackerService(ExporterTracker trackerService) {
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
