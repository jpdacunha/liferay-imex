package com.liferay.imex.site.exporter;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactoryUtil;
import com.liferay.exportimport.kernel.configuration.constants.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.exporter.configuration.ImExSiteExporterPropsKeys;
import com.liferay.imex.site.model.ImexSite;
import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.imex.site.util.SiteCommonUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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
	
	private static final String MSG_PRIVATE_LAYOUTS_EXPORT = "Private layouts export";

	private static final String MSG_PUBLIC_LAYOUTS_EXPORT = "Public layouts export";

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
	public void doExport(User user, Properties config, File sitesDir, long companyId, Locale locale, List<ExporterRawContent> rawContentToExport, boolean debug) {
		
		reportService.getStartMessage(_log, "SITE export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_ENABLED));
		boolean privatePagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PRIVATE_PAGE_ENABLED));
		boolean publicPagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PUBLIC_PAGE_ENABLED));
		
		if (enabled) {
			
				try {
						
					List<Group> bddGroups = groupLocalService.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
					List<Group> includedGroups = manageSitesExclusions(config, bddGroups);
					
					List<Group> groups = manageSitesOrder(config, includedGroups);
					
					for (Group group : groups) {
											
						boolean isSite = group.isSite() && !group.getFriendlyURL().equals(GroupConstants.CONTROL_PANEL_FRIENDLY_URL);
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
			
				String groupName = GroupUtil.getGroupName(group, locale);
				String parentGroupIdFriendlyUrl = null;
				
				try {
					
					parentGroupIdFriendlyUrl = siteCommonService.getParentSiteFriendlyURL(companyId, group.getParentGroupId());	
					processor.write(new ImexSite(group, parentGroupIdFriendlyUrl), siteDir, FileNames.getSiteFileName(group, processor.getFileExtension()));
						
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
		
				doExportPublicAndPrivateLar(user, config, locale, debug, privatePagesEnabled, publicPagesEnabled, group, siteDir);
				
				reportService.getOK(_log, groupName, "SITE : "  + groupName);
				
			} else {
				reportService.getDNE(_log, siteDir.getAbsolutePath());
			}
			
		} else {
			_log.error("siteDir is null ...");						
		}	
		
	}

	private void doExportPublicAndPrivateLar(
			User user, 
			Properties config, 
			Locale locale, 
			boolean debug,
			boolean privatePagesEnabled, 
			boolean publicPagesEnabled, 
			Group group, 
			File siteDir) throws PortalException {
		
		boolean privateLayout;
		String itemMsg = "friendlyUrl:" + group.getFriendlyURL();
		String friendlyUrl = group.getFriendlyURL();
		
		//Exporting public layouts
		if (publicPagesEnabled) {
			if (isExcludedPublicLar(config, group)) {
				reportService.getDisabled(_log, MSG_PUBLIC_LAYOUTS_EXPORT, itemMsg);
			} else {
				reportService.getStartMessage(_log, "exporting PUBLIC LAR for group [" + friendlyUrl + "]");
				privateLayout = false;
				doExportLar(user, config, group, siteDir, locale, privateLayout, debug);
				reportService.getEndMessage(_log, "export of PUBLIC LAR for group [" + friendlyUrl + "]");
			}					
		} else {
			reportService.getDisabled(_log, MSG_PUBLIC_LAYOUTS_EXPORT);
		}
		
		//Exporting private layouts
		if (privatePagesEnabled) {
			if (isExcludedPrivateLar(config, group)) {
				reportService.getDisabled(_log, MSG_PRIVATE_LAYOUTS_EXPORT, itemMsg);
			} else {
				reportService.getStartMessage(_log, "exporting PRIVATE LAR for group [" + friendlyUrl + "]");
				privateLayout = true;
				doExportLar(user, config, group, siteDir, locale, privateLayout, debug);
				reportService.getEndMessage(_log, "export of PRIVATE LAR for group [" + friendlyUrl + "]");
			}
		} else {
			reportService.getDisabled(_log, MSG_PRIVATE_LAYOUTS_EXPORT);
		}
		
	}
	
	private boolean isExcludedPublicLar(Properties config, Group group) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_EXCLUDE_PUBLIC_LAYOUT_FRIENDLYURL_LIST));
		List<String> friendlyUrls = CollectionUtil.getList(stringList);	
		
		return SiteCommonUtil.isExcluded(friendlyUrls, group);
		
	}
	
	private boolean isExcludedPrivateLar(Properties config, Group group) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_EXCLUDE_PRIVATE_LAYOUT_FRIENDLYURL_LIST));
		List<String> friendlyUrls = CollectionUtil.getList(stringList);	
		
		return SiteCommonUtil.isExcluded(friendlyUrls, group);
		
	}

	private List<Group> manageSitesOrder(Properties config, List<Group> groups) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_ORDER_FRIENDLYURL_LIST));
		List<String> friendlyUrls = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.managePriority(friendlyUrls, groups);
		
	}
	
	private List<Group> manageSitesExclusions(Properties config, List<Group> groups) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_EXCLUDE_FRIENDLYURL_LIST));
		List<String> friendlyUrlsToExclude = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.manageExclusions(friendlyUrlsToExclude, groups);
		
	}
	
	private void doExportLar(User user, Properties config, Group group, File siteDir, Locale locale, boolean privateLayout, boolean debug) throws PortalException {
		
		int exportType = ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT;
		long groupId = group.getGroupId();
		long userId = user.getUserId();
		String name = "IMEX : site export process";
		String description = "This an automatic site export triggered by IMEX";
		
		long[] layoutIds = SiteCommonUtil.ALL_LAYOUTS;
		
		//Standard parameter map
		Map<String, String[]> parameterMap = builParameterMapFromConfig(config, privateLayout, group);
		
		Map<String, Serializable> settingsMap = ExportImportConfigurationSettingsMapFactoryUtil.buildExportLayoutSettingsMap(user, groupId, privateLayout, layoutIds, parameterMap);

		ExportImportConfiguration exportImportConfiguration = larService.createExportImportConfiguration(groupId, userId, name, description, exportType, settingsMap, new ServiceContext());
		
		String fileName = FileNames.getLarSiteFileName(group, privateLayout);
		
		larService.doExport(exportImportConfiguration, siteDir, fileName);
			
	}
	
	private Map<String, String[]> builParameterMapFromConfig(Properties config, boolean privateLayout, Group group) {
		
		Map<String, String[]> parameterMap = new HashMap<>();
	
		String standardPrefix = EXPORT_SITE_PUBLIC_PAGE_PARAMETER_PREFIX;
		if (privateLayout) {
			standardPrefix = EXPORT_SITE_PRIVATE_PAGE_PARAMETER_PREFIX;
		}
	
		Map<String, String[]> standardParametersMap = larService.buildParameterMapFromProperties(config, standardPrefix);
		
		String friendlyUrl = group.getFriendlyURL();
		String overridePrefix = friendlyUrl.replaceAll(StringPool.SLASH, StringPool.BLANK) + StringPool.PERIOD;
		Map<String, String[]> groupOverridedParametersMap = larService.buildParameterMapFromProperties(config, overridePrefix + standardPrefix);
		
		if (groupOverridedParametersMap != null && groupOverridedParametersMap.size() > 0) {
			parameterMap.putAll(groupOverridedParametersMap);
			reportService.getMessage(_log, "group [" + friendlyUrl + "] is using SPECIFIC parameter map : [" + CollectionUtil.toString(parameterMap) + "]");
		} else {
			parameterMap.putAll(standardParametersMap);
			reportService.getMessage(_log, "group [" + friendlyUrl + "] is using STANDARD parameter map : [" + CollectionUtil.toString(parameterMap) + "]");
		}
		
		return parameterMap;
		 
	}
	
	@Override
	public void deploy() {
		
	}

	@Override
	public void undeploy() {
		
	}
	
	/**
	 * Return root directory name
	 *
	 */
	@Override
	public String getRootDirectoryName() {
		
		return FileNames.DIR_SITE;

	}
	
	/**
	 * Create directory for single item
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleSiteExportDirectory(File sitesDir, Group group, Locale locale) throws ImexException {
		
		String name = GroupUtil.getGroupFriendlyUrlAsName(group);
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