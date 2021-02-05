package com.liferay.imex.virtualhost.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ProfiledExporter;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.virtualhost.FileNames;
import com.liferay.imex.virtualhost.exporter.configuration.ImExVirtualhostExporterPropsKeys;
import com.liferay.imex.virtualhost.model.ImexVirtualhost;
import com.liferay.imex.virtualhost.service.VirtualhostCommonService;
import com.liferay.petra.string.StringPool;
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
import com.liferay.portal.kernel.service.VirtualHostLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.List;
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
public class VirtualhostExporter implements ProfiledExporter {
	
	public static final String DESCRIPTION = "Virtualhost exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(VirtualhostExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualHostLocalService virtualHostLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected LayoutSetLocalService layoutSetLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualhostCommonService virtualHostCommonService;

	@Override
	public void doExport(User user, Properties config, File virtualhostDir, long companyId, Locale locale, List<ExporterRawContent> rawContentToExport, boolean debug) {
	
		reportService.getStartMessage(_log, "Virtualhost export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExVirtualhostExporterPropsKeys.EXPORT_VIRTUALHOST_ENABLED));
		
		if (enabled) {
			
			try {
				
				Company company = companyLocalService.getCompany(companyId);
				List<VirtualHost> hosts  = virtualHostCommonService.getCompanyVirtualHost(companyId);
				
				for (VirtualHost virtualHost : hosts) {
					
					String hostname = virtualHost.getHostname();
					
					try {
						
						Group group = null;
						boolean publicVirtualHost = true;
						long layoutSetId = virtualHost.getLayoutSetId();
						boolean defaultVirtualHost = virtualHost.getDefaultVirtualHost();
						String groupFriendlyURL = StringPool.BLANK;
						String languageId = StringPool.BLANK;
						boolean isCompanyLayoutSet = layoutSetId == VirtualhostCommonService.DEFAULT_LAYOUTSET_ID;
						
						if (!isCompanyLayoutSet) {
							
							LayoutSet layoutSet = layoutSetLocalService.getLayoutSet(layoutSetId);
							publicVirtualHost =  !layoutSet.isPrivateLayout();
							group = groupLocalService.getGroup(layoutSet.getGroupId());
							groupFriendlyURL = group.getFriendlyURL();
							languageId = virtualHost.getLanguageId();
							
						}
					
						String companyWebId = company.getWebId();
						
						ImexVirtualhost imexVirtualhost = new ImexVirtualhost(
								companyWebId, 
								groupFriendlyURL, 
								publicVirtualHost, 
								isCompanyLayoutSet, 
								hostname,
								defaultVirtualHost,
								languageId);
						
						processor.write(imexVirtualhost, virtualhostDir, FileNames.getVirtualhostFileName(virtualHost, company, group, processor.getFileExtension()));
						reportService.getOK(_log, "Virtualhost : "  + hostname + " for group : " + groupFriendlyURL);
						
					} catch (Exception e) {
						reportService.getError(_log, "Virtualhost : "  + hostname, e);
						if (debug) {
							_log.error(e,e);
						}
					}
					
				}
				
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
	public String getRootDirectoryName() {
		
		return FileNames.DIR_VIRTUALHOST;

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

	public VirtualHostLocalService getVirtualHostLocalService() {
		return virtualHostLocalService;
	}

	public void setVirtualHostLocalService(VirtualHostLocalService virtualHostLocalService) {
		this.virtualHostLocalService = virtualHostLocalService;
	}

	public GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public LayoutSetLocalService getLayoutSetLocalService() {
		return layoutSetLocalService;
	}

	public void setLayoutSetLocalService(LayoutSetLocalService layoutSetLocalService) {
		this.layoutSetLocalService = layoutSetLocalService;
	}

	public CompanyLocalService getCompanyLocalService() {
		return companyLocalService;
	}

	public void setCompanyLocalService(CompanyLocalService companyLocalService) {
		this.companyLocalService = companyLocalService;
	}

	public VirtualhostCommonService getVirtualHostCommonService() {
		return virtualHostCommonService;
	}

	public void setVirtualHostCommonService(VirtualhostCommonService virtualHostCommonService) {
		this.virtualHostCommonService = virtualHostCommonService;
	}

}
