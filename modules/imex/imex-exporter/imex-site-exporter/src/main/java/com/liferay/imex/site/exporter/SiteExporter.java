package com.liferay.imex.site.exporter;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactoryUtil;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.exporter.configuration.ImExSiteExporterPropsKeys;
import com.liferay.imex.site.model.ImExSite;
import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.imex.site.util.SiteCommonUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author dev
 */
@Component(
	immediate = true,
	property = {
			"imex.component.execution.priority=10",
			"imex.component.description=Site exporter",
			"service.ranking:Integer=10"
		},
	service = Exporter.class
)
public class SiteExporter implements Exporter {
	
	private static final String EXPORT_SITE_PRIVATE_PAGE_PARAMETER_PREFIX = "export.site.private.page.parameter.";

	private static final String EXPORT_SITE_PUBLIC_PAGE_PARAMETER_PREFIX = "export.site.public.page.parameter.";

	public static final String DESCRIPTION = "SITE exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(SiteExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexLarService larService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected SiteCommonService siteCommonService;

	@Override
	public void doExport(User user, Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "SITE export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_ENABLED));
		boolean privatePagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PRIVATE_PAGE_ENABLED));
		boolean publicPagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PUBLIC_PAGE_ENABLED));
		
		if (enabled) {
			
				try {
					
					File sitesDir = initializeExportDirectory(destDir);
					
					List<Group> bddGroups = groupLocalService.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
					List<Group> includedGroups = manageSitesExclusions(config, bddGroups);
					
					List<Group> groups = manageSitesOrder(config, includedGroups);
					
					for (Group group : groups) {
											
						boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
						if (isSite) {
							
							reportService.getStartMessage(_log, group, locale);
							doExport(companyId, user, config, locale, debug, privatePagesEnabled, publicPagesEnabled, sitesDir, group);
							reportService.getEndMessage(_log, group, locale);
							
						} else {
							
							String groupName = GroupUtil.getGroupName(group, locale);
							_log.debug("Skipping [" + groupName+ "] because it's not a site.");
							if (debug) {
								reportService.getSkipped(_log, groupName);
							}
							
						}
					}

				} catch (ImexException e) {
					_log.error(e,e);
					reportService.getError(_log, e);
				} catch (PortalException e) {
					_log.error(e,e);
				}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "SITE export process");
		
	}
	
	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}
	
	private void doExport(long companyId, User user, Properties config, Locale locale, boolean debug, boolean privatePagesEnabled, boolean publicPagesEnabled, File sitesDir, Group group) throws PortalException, ImexException { 
		
		File siteDir = initializeSingleSiteExportDirectory(sitesDir, group, locale);
		
		if (siteDir != null) {
			
			if (siteDir.exists()) {
			
				boolean privateLayout;
				String groupName = GroupUtil.getGroupName(group, locale);
				String parentGroupIdFriendlyUrl = null;
				
				try {
					
					parentGroupIdFriendlyUrl = siteCommonService.getParentSiteFriendlyURL(companyId, group.getParentGroupId());	
					processor.write(new ImExSite(group, parentGroupIdFriendlyUrl), siteDir, FileNames.getSiteFileName(group, processor.getFileExtension()));
						
				} catch (NoSuchGroupException e1) {
					reportService.getError(_log, groupName, "Site identified by [" + parentGroupIdFriendlyUrl + "] is a parent site an it does not exists. Maybe you can use [" + ImExSiteExporterPropsKeys.EXPORT_SITE_ORDER_FRIENDLYURL_LIST + "] to configure IMEX to import the parent site prior to child site.");
					if (debug) {
						_log.error(e1,e1);
					}					
					
				} catch (Exception e) {
					reportService.getError(_log, groupName, e);
					if (debug) {
						_log.error(e,e);
					}
				}
				
				if (privatePagesEnabled) {
					privateLayout = true;
					doExportLar(user, config, group, siteDir, locale, privateLayout, debug);
				}
		
				if (publicPagesEnabled) {
					privateLayout = false;
					doExportLar(user, config, group, siteDir, locale, privateLayout, debug);
				}
				
				reportService.getOK(_log, groupName, "SITE : "  + groupName);
				
			} else {
				reportService.getDNE(_log, siteDir.getAbsolutePath());
			}
			
		} else {
			_log.error("siteDir is null ...");						
		}	
		
	}

	private List<Group> manageSitesOrder(Properties config, List<Group> groups) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_ORDER_FRIENDLYURL_LIST));
		List<String> friendlyUrlsToExclude = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.managePriority(friendlyUrlsToExclude, groups);
		
	}
	
	private List<Group> manageSitesExclusions(Properties config, List<Group> groups) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_EXCLUDE_FRIENDLYURL_LIST));
		List<String> friendlyUrlsToExclude = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.manageExclusions(friendlyUrlsToExclude, groups);
		
	}
	
	
	private void doExportLar(User user, Properties config, Group group, File siteDir, Locale locale, boolean privateLayout, boolean debug) throws PortalException {
		
		int exportType = ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT;
		
		String prefix = EXPORT_SITE_PUBLIC_PAGE_PARAMETER_PREFIX;
		if (privateLayout) {
			prefix = EXPORT_SITE_PRIVATE_PAGE_PARAMETER_PREFIX;
		}
		
		long groupId = group.getGroupId();
		long userId = user.getUserId();
		String name = "IMEX : site export process";
		String description = "This an automatic site export triggered by IMEX";
		
		long[] layoutIds = null;
		
		Map<String, String[]> parameterMap = larService.buildParameterMapFromProperties(config, prefix);
		
		Map<String, Serializable> settingsMap = ExportImportConfigurationSettingsMapFactoryUtil.buildExportLayoutSettingsMap(user, groupId, privateLayout, layoutIds, parameterMap);

		ExportImportConfiguration exportImportConfiguration = larService.createExportImportConfiguration(groupId, userId, name, description, exportType, settingsMap, new ServiceContext());
		
		String fileName = FileNames.getLarSiteFileName(group, privateLayout);
		
		larService.doExport(exportImportConfiguration, siteDir, fileName);
			
	}
	
	/**
	 * Create root directory
	 * @param exportDir
	 * @return
	 * @throws ImexException
	 */
	private File initializeExportDirectory(File exportDir) throws ImexException {
		
		File sitesDir = new File(exportDir, FileNames.DIR_SITE);
		boolean success = sitesDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + sitesDir);
		}
		
		return sitesDir;
		
	}
	
	/**
	 * Create directory for single item
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleSiteExportDirectory(File sitesDir, Group group, Locale locale) throws ImexException {
		
		String name = GroupUtil.getGroupName(group, locale);
		File dir = new File(sitesDir, name);
		dir.mkdirs();		
		return dir;
		
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

	public ImexLarService getLarService() {
		return larService;
	}

	public void setLarService(ImexLarService larService) {
		this.larService = larService;
	}
	
}