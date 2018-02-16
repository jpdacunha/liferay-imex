package com.liferay.imex.wcddm.importer;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.exception.NoSuchTemplateException;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.enums.ImexOperationEnum;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.wcddm.FileNames;
import com.liferay.imex.wcddm.importer.configuration.ImExWCDDmImporterPropsKeys;
import com.liferay.imex.wcddm.importer.service.ImportWcDDMService;
import com.liferay.imex.wcddm.model.ImExStructure;
import com.liferay.imex.wcddm.model.ImExTemplate;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=20",
			"imex.component.description=Web Content DDM importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class WcDDMImporter implements Importer {
	
	private static final Log _log = LogFactoryUtil.getLog(WcDDMImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportWcDDMService importWcDDMService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	
	private DDM _ddm;

	@Override
	public void doImport(ServiceContext serviceContext, User user, Properties config, File companyDir, long companyId, Locale locale, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("Web Content DDM import process"));
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmImporterPropsKeys.IMPORT_WCDDM_ENABLED));
		
		if (enabled) {
		
			try {
				
				File dir = getWCDDMImportDirectory(companyDir);
				
				File[] groupDirs = FileUtil.listFiles(dir);
				for (File groupDir : groupDirs) {
					this.doImport(serviceContext, companyId, user, config, groupDir, locale, debug);
				}
				
				
			} catch (Exception e) {
				_log.error(e,e);
				_log.error(MessageUtil.getErrorMessage(e)); 
			}
			
		} else {
			_log.info(MessageUtil.getDisabled("Web Content DDM import"));
		}
		
		_log.info(MessageUtil.getEndMessage("Web Content DDM import process"));		
	}
	
	private void doImport(ServiceContext serviceContext, long companyId, User user, Properties config, File groupDir, Locale locale, boolean debug) {
			
		if (groupDir != null) {
			
			if (groupDir.exists()) {
				
				String groupFriendlyUrl = ImexNormalizer.getFriendlyURLByDirName(groupDir.getName());
				
				Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(companyId, groupFriendlyUrl);
				
				if (group != null) {
					
					_log.info(MessageUtil.getStartMessage(group, locale));
					
					File[] structuresDirs = FileUtil.listFiles(groupDir);
					
					//For each structure dir
					for (File structureDir : structuresDirs) {
						
						DDMStructure structure = doImportStructure(serviceContext, debug, group, user, locale, structureDir);
						
						doImportTemplate(serviceContext, debug, group, user, locale, structureDir, structure);
						_log.info(MessageUtil.getSeparator());
						
					}
					
					_log.info(MessageUtil.getEndMessage(group, locale));
					
				} else {
					_log.warn(MessageUtil.getDNE(groupFriendlyUrl));
				}
				
			} else {
				_log.warn(MessageUtil.getDNE(groupDir.getAbsolutePath()));
			}
			
			
		} else {
			_log.error("Skipping null roleDir ...");
		}
	}

	private DDMStructure doImportStructure(ServiceContext serviceContext, boolean debug, Group group, User user, Locale locale, File structureDir) {
		
		DDMStructure structure = null;
		String structurefileBegin = FileNames.getStructureFileNameBegin();
		ServiceContext serviceContextStr = (ServiceContext)serviceContext.clone();
		
		File[] structureFiles = FileUtil.listFiles(structureDir, structurefileBegin);
	
		if (structureFiles != null && structureFiles.length == 1) {
			
			String structureFileName = structureFiles[0].getName();
			File structureFile = new File(structureDir, structureFileName);
			
			ImexOperationEnum operation = ImexOperationEnum.UPDATE;
			
			try {

				ImExStructure imexStructure = (ImExStructure)processor.read(ImExStructure.class, structureFile);
				
				Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(imexStructure.getName());
				Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(imexStructure.getDescription());
				long userId = user.getUserId();
				long groupId = group.getGroupId();
				serviceContextStr.setAddGroupPermissions(true);
				serviceContextStr.setAddGuestPermissions(true);
							
				DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(imexStructure.getData());
				DDMFormLayout ddmFormLayout = _ddm.getDefaultDDMFormLayout(ddmForm);
				
				try {
					
					structure = DDMStructureLocalServiceUtil.getDDMStructureByUuidAndGroupId(imexStructure.getUuid(), group.getGroupId());
					
					long parentStructureId = structure.getParentStructureId();
					long classNameId = structure.getClassNameId();
					String structureKey = structure.getStructureKey();
					
					structure = DDMStructureLocalServiceUtil.updateStructure(userId, groupId, parentStructureId , classNameId, structureKey, nameMap, descriptionMap, ddmForm, ddmFormLayout, serviceContextStr);
					
				} catch (NoSuchStructureException e) {
						
					long classNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class);

					long parentStructureId = DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID;
					String structureKey = "imex-" + imexStructure.getKey() + "-" + CounterLocalServiceUtil.increment();
					String storageType = imexStructure.getStorageType();
					int type = imexStructure.getStructureType();
					//uuid is set only for creation
					serviceContextStr.setUuid(imexStructure.getUuid());
					
					structure = DDMStructureLocalServiceUtil.addStructure(
							userId, 
							groupId, 
							parentStructureId, 
							classNameId,
							structureKey, 
							nameMap,
							descriptionMap, 
							ddmForm,
							ddmFormLayout, 
							storageType, 
							type,
							serviceContextStr);
					
					operation = ImexOperationEnum.CREATE;
						
				}
				
				String groupName = GroupUtil.getGroupName(group, locale);
				_log.info(MessageUtil.getOK(groupName, "STRUCTURE : "  + structure.getName(locale), structureFile, operation));
				
			}  catch (Exception e) {
				_log.error(MessageUtil.getError(structureFile.getName(), e.getMessage()));
				if (debug) {
					_log.error(e,e);
				}
			}
			
		} else {
			_log.info(MessageUtil.getError("Wrong number of files", "[" + structureDir.getPath() + "] cannot contain more than one file matching [" + structurefileBegin + "]"));
		}
		
		return structure;
		
	}
	
	private DDMTemplate doImportTemplate(ServiceContext serviceContext, boolean debug, Group group, User user, Locale locale, File structureDir, DDMStructure structure) {
		
		DDMTemplate template = null;
		String templatefileBegin = FileNames.getTemplateFileNameBegin();
		File[] templateFiles = FileUtil.listFiles(structureDir, templatefileBegin);
		String groupName = GroupUtil.getGroupName(group, locale);	
		ServiceContext serviceContextTem = (ServiceContext)serviceContext.clone();
		
		if (templateFiles != null) {	
			
			for (File matchingTemplateFile : Arrays.asList(templateFiles)) {
				
				String templateFileName = matchingTemplateFile.getName();
				File templateFile = new File(structureDir, templateFileName);
				ImexOperationEnum operation = ImexOperationEnum.UPDATE;
				
				try {
					
					ImExTemplate imexTemplate = (ImExTemplate)processor.read(ImExTemplate.class, templateFile);
					
					Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(imexTemplate.getName());
					Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(imexTemplate.getDescription());
					long userId = user.getUserId();
					String type = imexTemplate.getTemplateType();
					String mode = null;
					String language = imexTemplate.getLangType();
					String script = imexTemplate.getData();
					boolean cacheable = imexTemplate.isCacheable();
					long classPK = 0;
					if (structure != null) {
						classPK = structure.getStructureId();
					}
					
					serviceContextTem.setAddGroupPermissions(true);
					serviceContextTem.setAddGuestPermissions(true);
					serviceContextTem.setUuid(imexTemplate.getUuid());
					
					try {
						
						//Searching for existing template
						template = DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(imexTemplate.getUuid(), group.getGroupId());
						
						long templateId = template.getTemplateId();
						
						DDMTemplateLocalServiceUtil.updateTemplate(userId, templateId, classPK, nameMap, descriptionMap, type, mode, language, script, cacheable, serviceContextTem);
						
					} catch (NoSuchTemplateException e) {
												
						long classNameId = ClassNameLocalServiceUtil.getClassNameId(DDMStructure.class);
						long resourceClassNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class);
						long groupId = group.getGroupId();
						String templateKey = "imex-" + imexTemplate.getKey() + "-" + CounterLocalServiceUtil.increment();
						
						boolean smallImage = false;
						String smallImageURL = null;
						File smallImageFile = null;
						
						template = DDMTemplateLocalServiceUtil.addTemplate(
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
					
					_log.info(MessageUtil.getOK(groupName, "[TEMPLATE : " + template.getName(locale) + " => STRUCTURE : " + structure.getName(locale) + "]", templateFile, operation));
					
				} catch (Exception e) {
					_log.error(MessageUtil.getError(templateFile.getName(), e.getMessage()));
					if (debug) {
						_log.error(e,e);
					}
				}
			}
			
		} else {
			_log.info(MessageUtil.getMessage("Missing templates", "No files found matching [" + templatefileBegin + "]"));
		}
		
		return template;
	}
	

	private File getWCDDMImportDirectory(File companyDir) {
		
		File dir = new File(companyDir, FileNames.DIR_WCDDM);
		
		if (dir.exists()) {
			return dir;
		} else {
			_log.warn(MessageUtil.getDNE(dir));
		}
		
		return null;
		
	}

	@Override
	public String getProcessDescription() {
		return "Web Content DDM import";
	}
	
	@Reference(unbind = "-")
	protected void setDDM(DDM ddm) {
		_ddm = ddm;
	}
	
	@Reference(unbind = "-")
	protected void setDDMFormJSONDeserializer(DDMFormJSONDeserializer ddmFormJSONDeserializer) {
		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
	}
	
	

}
