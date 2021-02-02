package com.liferay.imex.site.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactoryUtil;
import com.liferay.exportimport.kernel.exception.LARFileNameException;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.importer.configuration.ImExSiteImporterPropsKeys;
import com.liferay.imex.site.importer.service.ImportSiteBehaviorManagerService;
import com.liferay.imex.site.model.ImexSite;
import com.liferay.imex.site.model.OnExistsSiteMethodEnum;
import com.liferay.imex.site.model.OnMissingSiteMethodEnum;
import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.imex.site.util.SiteCommonUtil;
import com.liferay.portal.kernel.exception.DuplicateGroupException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=100",
			"imex.component.description=Site importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class SiteImporter implements Importer {
	
	private static final String DESCRIPTION = "SITE import";
	
	private static final String IMPORT_SITE_PRIVATE_PAGE_PARAMETER_PREFIX = "import.site.private.page.parameter.";

	private static final String IMPORT_SITE_PUBLIC_PAGE_PARAMETER_PREFIX = "import.site.public.page.parameter.";

	private static final Log _log = LogFactoryUtil.getLog(SiteImporter.class);

	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CounterLocalService counterLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportSiteBehaviorManagerService behaviorManagerService;
		
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected SiteCommonService siteCommonService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexLarService larService;

	@Override
	public void doImport(Bundle bundle, ServiceContext serviceContext, User user, Properties config, File dir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "SITE import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_ENABLED));
		
		if (enabled) {
			
			Map<String, String> toUpdateParentGroups = new HashMap<String, String>();
		
			try {
				
				File[] filesystemGroupDirs = FileUtil.listFiles(dir);
				
				File[] includedArray = manageSitesExclusions(config, filesystemGroupDirs);
				
				File[] groupDirs = manageSitesOrder(config, includedArray);
				
				//Updating site datas and importing LAR
				doUpdateSitesDatas(companyId, user, config, groupDirs, locale, toUpdateParentGroups, debug);
				
				//Updating site hierarchie
				doUpdateSitesHierarchy(companyId, toUpdateParentGroups, debug);

			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "SITE import process");		
	}
	
	private void doUpdateSitesDatas(long companyId, User user, Properties config, File[] groupDirs, Locale locale, Map<String, String> toUpdateParentGroups, boolean debug) {
		
		for (File groupDir : groupDirs) {
			
			try {
				this.doUpdateSiteDatas(companyId, user, config, groupDir, locale, toUpdateParentGroups, debug);
			} catch (Exception e) {
				reportService.getError(_log, groupDir.getAbsolutePath(), e);
				if (debug) {
					_log.error(e,e);
				}
			}
				
		}
	
	}
	
	private void doUpdateSitesHierarchy(long companyId, Map<String, String> toUpdateParentGroups, boolean debug) {
		
		reportService.getStartMessage(_log, "Managing sites hierarchy");
		
		if (toUpdateParentGroups.size() == 0) {
			reportService.getEmpty(_log, "no hierarchy to manage");
		} else {
			
			for (Entry<String, String> entry : toUpdateParentGroups.entrySet()) {
				
				String friendlyURL = null;
				try {
					
					friendlyURL = entry.getKey();
					String parentGroupIdFriendlyUrl = entry.getValue();
					Group group = groupLocalService.getFriendlyURLGroup(companyId, friendlyURL);
					siteCommonService.attachToParentSite(group, parentGroupIdFriendlyUrl);
					
				} catch (Exception e) {
					reportService.getError(_log, friendlyURL, e);
					if (debug) {
						_log.error(e,e);
					}
				}
				
			}	
			
		}
		
		reportService.getEndMessage(_log, "Managing sites hierarchy");
		
	}

	private void doUpdateSiteDatas(long companyId, User user, Properties config, File groupDir, Locale locale, Map<String, String> toUpdateParentGroups, boolean debug) throws Exception {
			
		if (groupDir != null) {
			
			if (groupDir.exists()) {
				
				String dirName = groupDir.getName();
				String groupFriendlyUrl = ImexNormalizer.getFriendlyURLByDirName(dirName);
				
				reportService.getStartMessage(_log, groupFriendlyUrl);
				
				String siteDescriptorFileName = FileNames.getSiteFileName(groupFriendlyUrl, processor.getFileExtension());
				
				ImexSite imexSite = (ImexSite)processor.read(ImexSite.class, groupDir, siteDescriptorFileName);
				
				Group group = null;
				
				UnicodeProperties typeSettingsProperties = imexSite.getUnicodeProperties();
				ServiceContext serviceContext = new ServiceContext();
				
				boolean site = imexSite.isSite();
				long userId = user.getUserId();
				String className = imexSite.getClassName();
				long classPK = imexSite.getClassPK();
				Map<Locale, String> nameMap = imexSite.getNameMap();
				Map<Locale, String> descriptionMap = imexSite.getDescriptionMap();
				int type = imexSite.getType();
				String friendlyURL = imexSite.getFriendlyURL();
				boolean active = imexSite.isActive();
				int membershipRestriction = imexSite.getMemberShipRestriction();
				boolean manualMembership = imexSite.isManualMemberShip();
				boolean inheritContent = imexSite.isInheritContent();
				
				long liveGroupId = GroupConstants.DEFAULT_LIVE_GROUP_ID;
				long defaultParentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
				
				String logPrefix = "SITE : "  + groupFriendlyUrl;
				
				try {
					
					OnMissingSiteMethodEnum createMethod = behaviorManagerService.getOnMissingBehavior(config, groupFriendlyUrl);

					if (createMethod.getValue().equals(OnMissingSiteMethodEnum.CREATE.getValue())) {
						
						group = groupLocalService.addGroup(userId, defaultParentGroupId, className, classPK, liveGroupId, nameMap, descriptionMap, type, manualMembership, membershipRestriction, friendlyURL, site, active, serviceContext);
						doImportLars(groupDir, config, user, group, locale, debug);
						groupLocalService.updateGroup(group.getGroupId(), typeSettingsProperties.toString());
						
					} else {
						_log.debug("Site creation were skipped.");
					}
					
					reportService.getOK(_log, dirName, logPrefix, createMethod.getValue());
					
				} catch(DuplicateGroupException e) {
					
					//Loading Liferay site
					group = groupLocalService.getFriendlyURLGroup(companyId, friendlyURL);
					
					OnExistsSiteMethodEnum duplicateMethod = behaviorManagerService.getOnExistsBehavior(config, group);
					
					if (!duplicateMethod.getValue().equals(OnExistsSiteMethodEnum.SKIP.getValue())) {

						long groupId = group.getGroupId();
						
						//Si le group existe
						if (duplicateMethod.getValue().equals(OnExistsSiteMethodEnum.UPDATE.getValue())) {
							
							groupLocalService.updateGroup(groupId, defaultParentGroupId, nameMap, descriptionMap, type, manualMembership, membershipRestriction, friendlyURL, inheritContent, active, serviceContext);
							
						} else if (duplicateMethod.getValue().equals(OnExistsSiteMethodEnum.RECREATE.getValue())) {
							
							//Reseting parent group
							siteCommonService.eraseSiteHierarchy(group);
							
							//Deleting site
							group = groupLocalService.deleteGroup(group);
												
							//Creating site again
							group = groupLocalService.addGroup(userId, defaultParentGroupId, className, classPK, liveGroupId, nameMap, descriptionMap, type, manualMembership, membershipRestriction, friendlyURL, site, active, serviceContext);
							
						}
						
						//Importing LARS
						doImportLars(groupDir, config, user, group, locale, debug);
						groupLocalService.updateGroup(group.getGroupId(), typeSettingsProperties.toString());
					
						reportService.getOK(_log, dirName, logPrefix, duplicateMethod.getValue());
						
					} else {
						reportService.getOK(_log, dirName, logPrefix, duplicateMethod.getValue());
					}
					
				}
				
				//Registering sites hierarchy to update
				String parentGroupIdFriendlyUrl = imexSite.getParentGroupIdFriendlyUrl();
				
				if (group != null && Validator.isNotNull(parentGroupIdFriendlyUrl)) {
					toUpdateParentGroups.put(groupFriendlyUrl, parentGroupIdFriendlyUrl);
				} else {
					_log.debug("Ste identified by [" + groupFriendlyUrl + "] has no parent group");
				}
				
				reportService.getEndMessage(_log, groupFriendlyUrl);
				
			} else {
				reportService.getDNE(_log, groupDir.getAbsolutePath());
			}
			
			
		} else {
			_log.error("Skipping null dir ...");
		}
	}
	
	private void doImportLars(File groupDir, Properties config, User user, Group group, Locale locale, boolean debug) throws Exception {
		
		boolean privatePagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_PRIVATE_PAGE_ENABLED));
		boolean publicPagesEnabled = GetterUtil.getBoolean(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_PUBLIC_PAGE_ENABLED));
		
		if (privatePagesEnabled) {
			
			doImportLar(groupDir, config, user, group, locale, debug, IMPORT_SITE_PRIVATE_PAGE_PARAMETER_PREFIX, true);
			
		} else {
			reportService.getDisabled(_log, "Private pages import");
		}
	

		if (publicPagesEnabled) {

			doImportLar(groupDir, config, user, group, locale, debug, IMPORT_SITE_PUBLIC_PAGE_PARAMETER_PREFIX, false);
			
		} else {
			reportService.getDisabled(_log, "Public pages import");
		}
			
			
	}
	
	private void doImportLar(File groupDir, Properties config, User user, Group group, Locale locale, boolean debug, String prefix, boolean privateLayout) throws Exception {
		
		long groupId = group.getGroupId();
		long userId = user.getUserId();
		String name = "IMEX : site import process";
		String description = "This an automatic site import triggered by IMEX";
		long[] layoutIds = SiteCommonUtil.ALL_LAYOUTS;
		Map<String, String[]> parameterMap = null;
		Map<String, Serializable> settingsMap = null;
		int importType = ExportImportConfigurationConstants.TYPE_IMPORT_LAYOUT;
		
		String larFileName = FileNames.getLarSiteFileName(group, privateLayout);
		
		try {
			
			parameterMap = larService.buildParameterMapFromProperties(config, prefix);
			settingsMap = ExportImportConfigurationSettingsMapFactoryUtil.buildImportLayoutSettingsMap(user, groupId, privateLayout, layoutIds, parameterMap);
			ExportImportConfiguration exportImportConfiguration = larService.createExportImportConfiguration(groupId, userId, name, description, importType, settingsMap, new ServiceContext());
			
			larService.doImport(user, exportImportConfiguration, groupDir, larFileName);
			
		} catch (LARFileNameException e) {
			
			reportService.getMessage(_log, group.getFriendlyURL(), "[" + larFileName + "] is missing in [" + groupDir.getAbsolutePath() + "]");
			if (debug) {
				_log.error(e,e);
			}
			
		}
	}
	
	private File[] manageSitesOrder(Properties config, File[] groupDirs) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_ORDER_FRIENDLYURL_LIST));
		List<String> friendlyUrlsToExclude = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.managePriority(friendlyUrlsToExclude, groupDirs);
		
	}
	
	private File[] manageSitesExclusions(Properties config, File[] groupDirs) {
		
		String stringList = GetterUtil.getString(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_EXCLUDE_FRIENDLYURL_LIST));
		List<String> friendlyUrlsToExclude = CollectionUtil.getList(stringList);
		
		return SiteCommonUtil.manageExclusions(friendlyUrlsToExclude, groupDirs);
		
	}
	
	/**
	 * Return root directory name
	 *
	 */
	@Override
	public String getRootDirectoryName() {
		
		return FileNames.DIR_SITE;

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


	public GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}


	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}


	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}


	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}


	public ImexExecutionReportService getReportService() {
		return reportService;
	}


	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}


	public ImportSiteBehaviorManagerService getBehaviorManagerService() {
		return behaviorManagerService;
	}


	public void setBehaviorManagerService(ImportSiteBehaviorManagerService behaviorManagerService) {
		this.behaviorManagerService = behaviorManagerService;
	}


	public SiteCommonService getSiteCommonService() {
		return siteCommonService;
	}


	public void setSiteCommonService(SiteCommonService siteCommonService) {
		this.siteCommonService = siteCommonService;
	}


	public ImexLarService getLarService() {
		return larService;
	}


	public void setLarService(ImexLarService larService) {
		this.larService = larService;
	}

}
