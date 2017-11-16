package com.liferay.imex.core.service.importer.impl;

import com.liferay.imex.core.api.ImexConfigurationService;
import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.util.configuration.ImexPropsUtil;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;

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

@Component(immediate = true, service = ImexImportService.class)
public class ImexImportServiceImpl implements ImexImportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexImportServiceImpl.class);
	
	private ImporterTracker trackerService;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;

	@Override
	public void doImport() {
		
		_log.info(MessageUtil.getStartMessage("import process"));
		
		try {
			
			File importDir = getImportDirectory();
			
			_log.info(MessageUtil.getPropertyMessage("IMEX import path", importDir.toString()));
			
			Map<String, ServiceReference<Importer>> Importers = trackerService.getImporters();
			
			List<Company> companies = companyLocalService.getCompanies();
			
			for (Company company : companies) {
				
				long companyId = company.getCompanyId();
				
				File companyDir = getCompanyImportDirectory(importDir, company);
				
				if (companyDir != null) {
					executeRegisteredImporters(Importers, companyDir, companyId);
				}
				
			}
			
		} catch (ImexException e) {
			_log.error(e,e);
		}
		
		_log.info(MessageUtil.getEndMessage("import process"));

	}

	private File getCompanyImportDirectory(File importDir, Company company) {
		
		String webId = company.getWebId();
		File companyDir = new File(importDir, webId);
		
		if (companyDir.exists()) {
			return companyDir;
		} else {
			_log.warn("[" + companyDir.getAbsolutePath() + "] does not exists");
		}
		
		return null;
	}

	private File getImportDirectory() throws ImexException {
		
		String importFilePath = configurationService.getImexDataPath();
		
		File importFile = new File(importFilePath);
		
		if (importFile.exists()) {
			return importFile;
		} else {
			throw new ImexException(importFile + " does not exists.");
		}
		
	}
	
	private void executeRegisteredImporters(Map<String, ServiceReference<Importer>> importers, File companyDir, long companyId) {
		
		if (importers != null && importers.size() > 0) {
			
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> reference = entry.getValue();
				
				Bundle bundle = reference.getBundle();
				
				Importer Importer = bundle.getBundleContext().getService(reference);
				
				_log.info(MessageUtil.getStartMessage(Importer.getProcessDescription(), 1));
				
				//Loading configuration for each Importer
				Properties config = configurationService.loadImporterConfiguration(bundle);
				
				if (config == null) {
					_log.warn(MessageUtil.getMessage(bundle, "has no defined configuration. Aborting execution ..."));
				}
				
	
				ImexPropsUtil.displayProperties(config, bundle);
				
				//FIXME : manage debug param
				Importer.doImport(config, companyDir, companyId, true);
									
				_log.info(MessageUtil.getEndMessage(Importer.getProcessDescription(), 1));
				
			}
			
		} else {
			_log.info(MessageUtil.getMessage("No registered Importers"));
		}
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(ImporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetImporterTracker(ImporterTracker trackerService) {
		this.trackerService = null;
	}

}
