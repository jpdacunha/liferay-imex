package com.liferay.imex.site.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.site.FileNames;
import com.liferay.imex.site.importer.configuration.ImExSiteImporterPropsKeys;
import com.liferay.imex.site.util.SiteCommonUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;

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
					
					_log.info(">>>>>>>>>>>>>>>>>>>> " + groupDir.getAbsolutePath());
					//this.doImport(bundle, serviceContext, companyId, user, config, groupDir, locale, debug);
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
	
	private void doImport(Bundle bundle, ServiceContext serviceContext, long companyId, User user, Properties config, File groupDir, Locale locale, boolean debug) {
			
		if (groupDir != null) {
			
			if (groupDir.exists()) {
				
				String groupFriendlyUrl = ImexNormalizer.getFriendlyURLByDirName(groupDir.getName());
				
				Group group = groupLocalService.fetchFriendlyURLGroup(companyId, groupFriendlyUrl);
				
				if (group != null) {
					
					reportService.getStartMessage(_log, group, locale);
					
					File[] adtsDirs = FileUtil.listFiles(groupDir);
					
					//For each structure dir
					for (File adtDir : adtsDirs) {
						
						doImportAdt(bundle, serviceContext, debug, group, user, locale, adtDir, config);
						reportService.getSeparator(_log);
						
					}
					
					reportService.getEndMessage(_log, group, locale);
					
				} else {
					reportService.getDNE(_log, groupFriendlyUrl);
				}
				
			} else {
				reportService.getDNE(_log, groupDir.getAbsolutePath());
			}
			
			
		} else {
			_log.error("Skipping null dir ...");
		}
	}
	
	private DDMTemplate doImportAdt(Bundle bundle, ServiceContext serviceContext, boolean debug, Group group, User user, Locale locale, File adtDir, Properties config) {
		
//		DDMTemplate template = null;
//		String adtfileBegin = FileNames.getSiteFileNameBegin();
//		ServiceContext serviceContextTem = (ServiceContext)serviceContext.clone();
//		String groupName = GroupUtil.getGroupName(group, locale);
//		
//		File[] adtFiles = FileUtil.listFiles(adtDir, adtfileBegin);
//	
//		if (adtFiles != null || (adtFiles != null && adtFiles.length == 0)) {	
//			
//			for (File matchingAdtFile : Arrays.asList(adtFiles)) {
//			
//				String templateFileName = matchingAdtFile.getName();
//				File adtFile = new File(adtDir, templateFileName);
//				ImexOperationEnum operation = ImexOperationEnum.UPDATE;
//				
//				try {
//					
//					ImExAdt imexAdt = (ImExAdt)processor.read(ImExAdt.class, adtFile);
//					
//					long classNameId = classNameLocalService.getClassNameId(imexAdt.getClassName());
//					
//					//Liferay trap here :-) : the ressourceClassNameId is the name of the portlet. Potential problem here if the class is moved in future Liferay versions. 
//					long resourceClassNameId = classNameLocalService.getClassNameId(RESOURCE_CLASSNAME);
//					
//					Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(imexAdt.getName());
//					Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(imexAdt.getDescription());
//					long userId = user.getUserId();
//					String type = imexAdt.getTemplateType();
//					String mode = null;
//					String language = imexAdt.getLangType();
//					String script = imexAdt.getData();
//					boolean cacheable = imexAdt.isCacheable();
//					long classPK = 0;
//					
//					serviceContextTem.setUuid(imexAdt.getUuid());
//					
//					serviceContextTem.setAddGroupPermissions(false);
//					serviceContextTem.setAddGuestPermissions(false);
//				
//					try {
//						
//						//Searching for existing template
//						template = dDMTemplateLocalService.getDDMTemplateByUuidAndGroupId(imexAdt.getUuid(), group.getGroupId());
//						
//						long templateId = template.getTemplateId();
//						
//						dDMTemplateLocalService.updateTemplate(userId, templateId, classPK, nameMap, descriptionMap, type, mode, language, script, cacheable, serviceContextTem);
//						
//					} catch (NoSuchTemplateException e) {
//												
//						long groupId = group.getGroupId();
//						String templateKey = "imex-" + imexAdt.getKey() + "-" + counterLocalService.increment();
//						
//						boolean smallImage = false;
//						String smallImageURL = null;
//						File smallImageFile = null;
//						
//						template = dDMTemplateLocalService.addTemplate(
//								userId, 
//								groupId, 
//								classNameId, 
//								classPK,
//								resourceClassNameId, 
//								templateKey,
//								nameMap, 
//								descriptionMap,
//								type, 
//								mode, 
//								language, 
//								script,
//								cacheable , 
//								smallImage, 
//								smallImageURL,
//								smallImageFile, 
//								serviceContextTem
//						);
//						
//						operation = ImexOperationEnum.CREATE;
//						
//					}
//					
//					//Setting ADT permissions
//					Resource resource = resourceLocalService.getResource(template.getCompanyId(), DDMTemplate.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, template.getTemplateId() + "");
//					permissionSetter.setPermissions(config, bundle, resource);
//					
//					reportService.getOK(_log, groupName, "ADT : "  + template.getName(locale), adtFile, operation);
//				
//				}  catch (Exception e) {
//					reportService.getError(_log, adtFile.getName(), e);
//					if (debug) {
//						_log.error(e,e);
//					}
//				}
//				
//			}
//			
//		} else {
//			reportService.getMessage(_log, "Missing templates", "No files found matching [" + adtfileBegin + "]");
//		}
//
//		return template;
		
		return null;
		
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
