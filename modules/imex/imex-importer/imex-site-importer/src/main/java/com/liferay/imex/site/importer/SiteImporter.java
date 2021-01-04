package com.liferay.imex.site.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.importer.configuration.ImExSiteImporterPropsKeys;
import com.liferay.imex.site.importer.service.ImportSiteBehaviorManagerService;
import com.liferay.imex.site.model.ImExSite;
import com.liferay.imex.site.model.OnMissingSiteMethodEnum;
import com.liferay.imex.site.util.SiteCommonUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

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
			"imex.component.execution.priority=100",
			"imex.component.description=SITE importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class SiteImporter implements Importer {
	
	private static final String DESCRIPTION = "SITE import";

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

	@Override
	public void doImport(Bundle bundle, ServiceContext serviceContext, User user, Properties config, File companyDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "SITE import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_ENABLED));
		
		if (enabled) {
		
			try {
				
				File dir = getSiteImportDirectory(companyDir);
				
				File[] filesystemGroupDirs = FileUtil.listFiles(dir);
				
				File[] includedArray = manageSitesExclusions(config, filesystemGroupDirs);
				
				File[] groupDirs = manageSitesOrder(config, includedArray);
				
				for (File groupDir : groupDirs) {
				
					this.doSiteImport(bundle, serviceContext, companyId, user, config, groupDir, locale, debug);
					
				}

			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "SITE import process");		
	}
	

	private void doSiteImport(Bundle bundle, ServiceContext serviceContext, long companyId, User user, Properties config, File groupDir, Locale locale, boolean debug) {
			
		if (groupDir != null) {
			
			if (groupDir.exists()) {
				
				String dirName = groupDir.getName();
				String groupFriendlyUrl = ImexNormalizer.getFriendlyURLByDirName(dirName);
				
				reportService.getStartMessage(_log, groupFriendlyUrl);
				
				String siteDescriptorFileName = FileNames.getSiteFileName(groupFriendlyUrl, processor.getFileExtension());
				
				try {
					
					ImExSite imexSite = (ImExSite)processor.read(ImExSite.class, groupDir, siteDescriptorFileName);
					
					boolean site = imexSite.isSite();
					long userId = user.getUserId();
					String className = imexSite.getClassName();
					String name = imexSite.getName();
					String description = imexSite.getDescription();
					int type = imexSite.getType().getIntValue();
					String friendlyURL = imexSite.getFriendlyURL();
					boolean active = imexSite.isActive();
					//TODO : JDA manage parent groupID for Phenix
					long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
					int membershipRestriction = GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION;
					boolean manualMembership = true;
					long liveGroupId = GroupConstants.DEFAULT_LIVE_GROUP_ID;
					
					//try {
						
						OnMissingSiteMethodEnum createMethod = behaviorManagerService.getOnMissingBehavior(config, groupFriendlyUrl);

						if (createMethod.getValue().equals(OnMissingSiteMethodEnum.CREATE.getValue())) {
							
//							group = GroupLocalServiceUtil.addGroup(userId, parentGroupId, className, classPK, liveGroupId, name, description, type, manualMembership, membershipRestriction, friendlyURL, site, active, serviceContext);
//							doImportLars(groupDir, options, userId, group);
//							GroupLocalServiceUtil.updateGroup(group.getGroupId(), typeSettingsProperties.toString());
							
							reportService.getOK(_log, dirName, "SITE : "  + groupFriendlyUrl, ImexOperationEnum.CREATE);
							
						} else {
							reportService.getOK(_log, dirName, "SITE : "  + groupFriendlyUrl, ImexOperationEnum.SKIPPED);
						}
						
					//} catch(DuplicateGroupException e) {
						
//						//Chargement du group
//						group = GroupLocalServiceUtil.getFriendlyURLGroup(companyId, friendlyURL);
//						
//						if (!duplicateMethod.getValue().equals(OnDuplicateMethodEnum.SKIP.getValue())) {
//
//							long groupId = group.getGroupId();
//							
//							//Si le group existe
//							if (duplicateMethod.getValue().equals(OnDuplicateMethodEnum.UPDATE.getValue())) {
//								
//								GroupLocalServiceUtil.updateGroup(groupId, group.getParentGroupId(), name, description, type, true, 0, friendlyURL, active, serviceContext);
//								
//							} else if (duplicateMethod.getValue().equals(OnDuplicateMethodEnum.REPLACE.getValue())) {
//								
//								//Suppression du Group
//								GroupLocalServiceUtil.deleteGroup(group);
//													
//								//Creation du group
//								group = GroupLocalServiceUtil.addGroup(userId, parentGroupId, className, classPK, liveGroupId, name, description, type, manualMembership, membershipRestriction, friendlyURL, site, active, serviceContext);
//								
//							}
//							
//							//Importing LARS
//							doImportLars(groupDir, options, userId, group);
//							GroupLocalServiceUtil.updateGroup(group.getGroupId(), typeSettingsProperties.toString());
//							
//							_log.info("[" + group.getFriendlyURL() + "] => ["  + duplicateMethod.getValue() + "] Done.");
//							
//						} else {
//							_log.info("[" + group.getFriendlyURL() + "] => ["  + duplicateMethod.getValue() + "] Done.");
//						}
						
					//}
					
				} catch (Exception e) {
					reportService.getError(_log, groupFriendlyUrl, e);
					if (debug) {
						_log.error(e,e);
					}
				}
				
				reportService.getEndMessage(_log, groupFriendlyUrl);
				
			} else {
				reportService.getDNE(_log, groupDir.getAbsolutePath());
			}
			
			
		} else {
			_log.error("Skipping null dir ...");
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
	
	private File getSiteImportDirectory(File companyDir) {
		
		File dir = new File(companyDir, FileNames.DIR_SITE);
			
		if (dir.exists()) {
			return dir;
		} else {
			reportService.getDNE(_log, dir);
		}
		
		return null;
		
	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

}
