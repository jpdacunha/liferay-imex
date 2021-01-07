package com.liferay.imex.adt.importer;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.dynamic.data.mapping.exception.NoSuchTemplateException;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.imex.adt.FileNames;
import com.liferay.imex.adt.importer.configuration.ImExAdtImporterPropsKeys;
import com.liferay.imex.adt.model.ImExAdt;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.permission.ImexModelPermissionSetter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=90",
			"imex.component.description=ADT importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class AdtImporter implements Importer {
	
	private static final String RESOURCE_CLASSNAME = "com.liferay.portlet.display.template.PortletDisplayTemplate";

	private static final String DESCRIPTION = "ADT import";

	private static final Log _log = LogFactoryUtil.getLog(AdtImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexModelPermissionSetter permissionSetter;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMTemplateLocalService dDMTemplateLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ClassNameLocalService classNameLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ResourceLocalService resourceLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CounterLocalService counterLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doImport(Bundle bundle, ServiceContext serviceContext, User user, Properties config, File companyDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "ADT import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExAdtImporterPropsKeys.IMPORT_ADT_ENABLED));
		
		if (enabled) {
		
			try {
				
				File dir = getAdtImportDirectory(companyDir);
				
				File[] groupDirs = FileUtil.listFiles(dir);
				for (File groupDir : groupDirs) {
					this.doImport(bundle, serviceContext, companyId, user, config, groupDir, locale, debug);
				}
				
				
			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "ADT import process");		
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
		
		DDMTemplate template = null;
		String adtfileBegin = FileNames.getAdtFileNameBegin();
		ServiceContext serviceContextTem = (ServiceContext)serviceContext.clone();
		String groupName = GroupUtil.getGroupName(group, locale);
		
		File[] adtFiles = FileUtil.listFiles(adtDir, adtfileBegin);
	
		if (adtFiles != null || (adtFiles != null && adtFiles.length == 0)) {	
			
			for (File matchingAdtFile : Arrays.asList(adtFiles)) {
			
				String templateFileName = matchingAdtFile.getName();
				File adtFile = new File(adtDir, templateFileName);
				ImexOperationEnum operation = ImexOperationEnum.UPDATE;
				
				try {
					
					ImExAdt imexAdt = (ImExAdt)processor.read(ImExAdt.class, adtFile);
					
					long classNameId = classNameLocalService.getClassNameId(imexAdt.getClassName());
					
					//Liferay trap here :-) : the ressourceClassNameId is the name of the portlet. Potential problem here if the class is moved in future Liferay versions. 
					long resourceClassNameId = classNameLocalService.getClassNameId(RESOURCE_CLASSNAME);
					
					Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(imexAdt.getName());
					Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(imexAdt.getDescription());
					long userId = user.getUserId();
					String type = imexAdt.getTemplateType();
					String mode = null;
					String language = imexAdt.getLangType();
					String script = imexAdt.getData();
					boolean cacheable = imexAdt.isCacheable();
					long classPK = 0;
					
					serviceContextTem.setUuid(imexAdt.getUuid());
					
					serviceContextTem.setAddGroupPermissions(false);
					serviceContextTem.setAddGuestPermissions(false);
				
					try {
						
						//Searching for existing template
						template = dDMTemplateLocalService.getDDMTemplateByUuidAndGroupId(imexAdt.getUuid(), group.getGroupId());
						
						long templateId = template.getTemplateId();
						
						dDMTemplateLocalService.updateTemplate(userId, templateId, classPK, nameMap, descriptionMap, type, mode, language, script, cacheable, serviceContextTem);
						
					} catch (NoSuchTemplateException e) {
												
						long groupId = group.getGroupId();
						String templateKey = "imex-" + imexAdt.getKey() + "-" + counterLocalService.increment();
						
						boolean smallImage = false;
						String smallImageURL = null;
						File smallImageFile = null;
						
						template = dDMTemplateLocalService.addTemplate(
								userId, 
								groupId, 
								classNameId, 
								classPK,
								resourceClassNameId, 
								templateKey,
								nameMap, 
								descriptionMap,
								type, 
								mode, 
								language, 
								script,
								cacheable , 
								smallImage, 
								smallImageURL,
								smallImageFile, 
								serviceContextTem
						);
						
						operation = ImexOperationEnum.CREATE;
						
					}
					
					//Setting ADT permissions
					Resource resource = resourceLocalService.getResource(template.getCompanyId(), DDMTemplate.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL, template.getTemplateId() + "");
					permissionSetter.setPermissions(config, bundle, resource);
					
					reportService.getOK(_log, groupName, "ADT : "  + template.getName(locale), adtFile, operation);
				
				}  catch (Exception e) {
					reportService.getError(_log, adtFile.getName(), e);
					if (debug) {
						_log.error(e,e);
					}
				}
				
			}
			
		} else {
			reportService.getMessage(_log, "Missing templates", "No files found matching [" + adtfileBegin + "]");
		}

		return template;
		
	}

	private File getAdtImportDirectory(File companyDir) {
		
		File dir = new File(companyDir, FileNames.DIR_ADT);
		
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
