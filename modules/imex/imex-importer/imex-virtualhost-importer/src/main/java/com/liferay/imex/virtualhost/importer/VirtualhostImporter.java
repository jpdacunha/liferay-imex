package com.liferay.imex.virtualhost.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ProfiledImporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.virtualhost.FileNames;
import com.liferay.imex.virtualhost.importer.configuration.ImExVirtualHostImporterPropsKeys;
import com.liferay.imex.virtualhost.model.ImexVirtualhost;
import com.liferay.imex.virtualhost.service.VirtualhostCommonService;
import com.liferay.portal.kernel.exception.NoSuchCompanyException;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.NoSuchVirtualHostException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.List;
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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualHostLocalService virtualHostLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected LayoutSetLocalService layoutSetLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CounterLocalService counterLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualhostCommonService virtualHostCommonService;

	@Override
	public void doImport(Bundle bundle, ServiceContext rootServiceContext, User exportUser, Properties config, File srcDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "Virtualhost import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExVirtualHostImporterPropsKeys.IMPORT_VIRTUALHOST_ENABLED));
		
		if (enabled) {
		
			try {
				
				File[] files = FileUtil.listFilesByExtension(srcDir, processor.getFileExtension());
				for (File virtualHostFile : files) {
					
					doImportVirtualHost(companyId, locale, virtualHostFile);
					
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

	private void doImportVirtualHost(long companyId, Locale locale, File virtualHostFile) throws Exception, PortalException {

		/**
		 * TOTO upgrade Liferay version and switch to manage defaultLayoutSet	public List<VirtualHost> updateVirtualHosts(long companyId, final long layoutSetId, TreeMap<String, String> hostnames) is not available because is introduced by 
		 * another fixpack. Use of this method in futur version instead of using 
		 */
		
		
		ImexVirtualhost virtualHostObj = (ImexVirtualhost)processor.read(ImexVirtualhost.class, virtualHostFile);
		
		String companyWebId = virtualHostObj.getCompanyWebId();
		
		String groupFriendlyURL = virtualHostObj.getGroupFriendlyURL();
		
		boolean isPublicVirtualHost = virtualHostObj.isPublicVirtualHost();
		
		if (Validator.isNotNull(companyWebId)) {
		
			try {
				
				//Verify if company exists
				Company company = companyLocalService.getCompanyByWebId(companyWebId);
				
				boolean isCompanyVirtualHost = virtualHostObj.isCompanyVirtualHost();
				
				String name = "Company : " + company.getName();
				
				long layoutSetId = VirtualhostCommonService.DEFAULT_LAYOUTSET_ID;
						
				if (!isCompanyVirtualHost) {
					
					//Verify if group exists
					Group group = groupLocalService.getFriendlyURLGroup(companyId, groupFriendlyURL);
					
					LayoutSet layoutSet = layoutSetLocalService.getLayoutSet(group.getGroupId(), !isPublicVirtualHost);
					
					layoutSetId = layoutSet.getLayoutSetId();
					
					name = "Site : " + GroupUtil.getGroupName(group, locale);
				}
					
				String hostname = virtualHostObj.getHostname();
				
				if (Validator.isNotNull(hostname)) {
				
					ImexOperationEnum operation = ImexOperationEnum.UPDATE;
					
					try {

						//To identify if virtualhost exists or not
						virtualHostLocalService.getVirtualHost(companyId, layoutSetId);
						
						virtualHostLocalService.updateVirtualHost(companyId, layoutSetId, hostname);
						
						
					} catch (NoSuchVirtualHostException e) {
						
						operation = ImexOperationEnum.CREATE;
						
						long virtualHostId = counterLocalService.increment();
						
						VirtualHost virtualHost = virtualHostLocalService.createVirtualHost(virtualHostId);
						virtualHost.setHostname(hostname);
						virtualHost.setLayoutSetId(layoutSetId);
						virtualHost.setCompanyId(companyId);
						virtualHostLocalService.addVirtualHost(virtualHost);
						
					}
					
					reportService.getOK(_log, name, "Virtual Host : "  + hostname, virtualHostFile, operation);
					
				} else {
					reportService.getError(_log, virtualHostFile.getName(), "missing required information hostname");
				}
																	
			} catch(NoSuchCompanyException e) {
				reportService.getDNE(_log, "company identified by [" + companyWebId + "]");
				reportService.getSkipped(_log, virtualHostFile.getName());
			} catch(NoSuchGroupException e) {
				reportService.getDNE(_log, "group identified by [" + groupFriendlyURL + "]");
				reportService.getSkipped(_log, virtualHostFile.getName());
			}
		
		} else {
			reportService.getSkipped(_log, virtualHostFile.getName());
		}
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

	public GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public CompanyLocalService getCompanyLocalService() {
		return companyLocalService;
	}

	public void setCompanyLocalService(CompanyLocalService companyLocalService) {
		this.companyLocalService = companyLocalService;
	}

	public VirtualHostLocalService getVirtualHostLocalService() {
		return virtualHostLocalService;
	}

	public void setVirtualHostLocalService(VirtualHostLocalService virtualHostLocalService) {
		this.virtualHostLocalService = virtualHostLocalService;
	}

	public LayoutSetLocalService getLayoutSetLocalService() {
		return layoutSetLocalService;
	}

	public void setLayoutSetLocalService(LayoutSetLocalService layoutSetLocalService) {
		this.layoutSetLocalService = layoutSetLocalService;
	}

	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	public VirtualhostCommonService getVirtualHostCommonService() {
		return virtualHostCommonService;
	}

	public void setVirtualHostCommonService(VirtualhostCommonService virtualHostCommonService) {
		this.virtualHostCommonService = virtualHostCommonService;
	}

}
