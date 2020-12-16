package com.liferay.imex.site.exporter;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationParameterMapFactoryUtil;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactoryUtil;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.exporter.configuration.ImExSiteExporterPropsKeys;
import com.liferay.imex.site.model.ImExSite;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
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

	@Override
	public void doExport(User user, Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "SITE export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_ENABLED));
		boolean privatePagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PRIVATE_PAGE_ENABLED));
		boolean publicPagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteExporterPropsKeys.EXPORT_SITE_PUBLIC_PAGE_ENABLED));
		
		if (enabled) {
			
				try {
					
					File sitesDir = initializeExportDirectory(destDir);
					
					List<Group> groups = groupLocalService.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
					for (Group group : groups) {
											
						boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
						if (isSite) {
							
							reportService.getStartMessage(_log, group, locale);
							doExport(user, config, locale, debug, privatePagesEnabled, publicPagesEnabled, sitesDir, group);
							reportService.getEndMessage(_log, group, locale);
							
						} else {
							
							String groupName = GroupUtil.getGroupName(group, locale);
							_log.debug("Skipping [" + groupName+ "] because it's not a site.");
							if (debug) {
								reportService.getSkipped(_log, groupName);
							}
							
						}
					}

					// TODO : JDA Global Scope Export
					
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

	
	private void doExport(User user, Properties config, Locale locale, boolean debug, boolean privatePagesEnabled, boolean publicPagesEnabled, File sitesDir, Group group) throws PortalException, ImexException { 
		
		
		File siteDir = initializeSingleSiteExportDirectory(sitesDir, group, locale);
		
		boolean privateLayout;
		String groupName = GroupUtil.getGroupName(group, locale);
		
		try {
			
			processor.write(new ImExSite(group), siteDir, FileNames.getSiteFileName(group, locale, processor.getFileExtension()));
			reportService.getOK(_log, groupName, "SITE : "  + groupName);
			
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
	}

	
	private void doExportLar(User user, Properties config, Group group, File adtDir, Locale locale, boolean privateLayout, boolean debug) throws PortalException {
		
		int exportType = ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT;
		
		long groupId = group.getGroupId();
		long userId = user.getUserId();
		String name = "IMEX : site export process";
		String description = "This an automatic site export triggered by IMEX";
		
		String dataStrategy = null; 
		Boolean deleteMissingLayouts = null;
		Boolean deletePortletData = null; 
		Boolean deletions = null;
		Boolean ignoreLastPublishDate = null; 
		Boolean layoutSetPrototypeLinkEnabled = null;
		Boolean layoutSetSettings = null; 
		Boolean logo = null; 
		Boolean permissions = null;
		Boolean portletConfiguration = null; 
		Boolean portletConfigurationAll = null;
		List<String> portletConfigurationPortletIds = null; 
		Boolean portletData = null;
		Boolean portletDataAll = null; 
		List<String> portletDataPortletIds = null;
		Boolean portletSetupAll = null; 
		List<String> portletSetupPortletIds = null;
		String range = null;
		Boolean themeReference = null;
		Boolean updateLastPublishDate = null;
		String userIdStrategy = null;
		
		Map<String, String[]> parameterMap = ExportImportConfigurationParameterMapFactoryUtil.buildParameterMap(
				dataStrategy, 
				deleteMissingLayouts, 
				deletePortletData, 
				deletions, 
				ignoreLastPublishDate, 
				layoutSetPrototypeLinkEnabled, 
				layoutSetSettings, 
				logo, 
				permissions, 
				portletConfiguration, 
				portletConfigurationAll, 
				portletConfigurationPortletIds, 
				portletData, 
				portletDataAll, 
				portletDataPortletIds, 
				portletSetupAll, 
				portletSetupPortletIds, 
				range, 
				themeReference, 
				updateLastPublishDate, 
				userIdStrategy);
		
		long[] layoutIds = null;
		
		Map<String, Serializable> settingsMap = ExportImportConfigurationSettingsMapFactoryUtil.buildExportLayoutSettingsMap(user, groupId, privateLayout, layoutIds, parameterMap);

		ExportImportConfiguration exportImportConfiguration = larService.createExportImportConfiguration(groupId, userId, name, description, exportType, settingsMap, new ServiceContext());
		
//		if (group != null) {
//			
//			long classNameId = classNameLocalService.getClassNameId(classType);			
//			List<DDMTemplate> adts = dDMTemplateLocalService.getTemplates(group.getGroupId(), classNameId);
//			
//			if (adts != null && adts.size() > 0) {
//			
//				File groupDir = initializeSingleGroupDirectory(groupsDir, group);
//				
//				if (groupDir != null) {
//					
//					if (groupDir.exists()) {
//				
//						//Iterate over structures
//						for(DDMTemplate ddmTemplate : adts){
//												
//							File adtDir = initializeSingleAdtExportDirectory(groupDir, classType);
//							
//							if (adtDir != null) {
//								
//								if (adtDir.exists()) {
//							
//									try {
//										
//										String groupName = GroupUtil.getGroupName(group, locale);
//										
//										processor.write(new ImExAdt(ddmTemplate, classType), adtDir, FileNames.getAdtFileName(ddmTemplate, group, locale, processor.getFileExtension()));
//										reportService.getOK(_log, groupName, "ADT : "  + ddmTemplate.getName(locale) + ", type : " + classType);
//										
//									} catch (Exception e) {
//										reportService.getError(_log, ddmTemplate.getName(locale), e);
//										if (debug) {
//											_log.error(e,e);
//										}
//									}
//							
//								} else {
//									reportService.getDNE(_log, adtDir.getAbsolutePath());
//								}
//								
//							} else {
//								_log.error("adtDir is null ...");						
//							}
//							
//						}
//						
//					} else {
//						reportService.getDNE(_log, groupDir.getAbsolutePath());
//					}
//					
//				} else {
//					_log.error("groupDir is null ...");
//				}
//				
//			} else {
//				reportService.getEmpty(_log, group, locale, classType);				
//			}
//			
//		} else {
//			_log.error("Skipping null group ...");
//		}
//		
	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
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
	
}