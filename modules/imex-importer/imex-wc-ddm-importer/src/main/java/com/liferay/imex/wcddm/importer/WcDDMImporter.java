package com.liferay.imex.wcddm.importer;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
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
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.util.Date;
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
						
						//DDMTemplate template = doImportTemplate(debug, group, user, locale, structureDir, structure);
						_log.info(StringPool.BLANK);
						
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
		
		File[] structureFiles = FileUtil.listFiles(structureDir, structurefileBegin);
	
		if (structureFiles != null && structureFiles.length == 1) {
			
			String structureFileName = structureFiles[0].getName();
			File structureFile = new File(structureDir, structureFileName);
			Date currentDate = new Date();
			
			ImexOperationEnum operation = ImexOperationEnum.UPDATE;
			
			try {

				ImExStructure imexStructure = (ImExStructure)processor.read(ImExStructure.class, structureFile);
				Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(imexStructure.getName());
				Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(imexStructure.getDescription());
				
				try {
					
					structure = DDMStructureLocalServiceUtil.getDDMStructureByUuidAndGroupId(imexStructure.getUuid(), group.getGroupId());
					// Structure update logic
					structure.setNameMap(nameMap);
					structure.setDescriptionMap(descriptionMap);
					structure.setDefinition(imexStructure.getData());
					structure.setModifiedDate(currentDate);
					
					DDMStructureLocalServiceUtil.updateDDMStructure(structure);
					
				} catch (NoSuchStructureException e) {
						
					long classNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class);
					long userId = user.getUserId();
					long groupId = group.getGroupId();
					long parentStructureId = DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID;
					String structureKey = "imex-" + imexStructure.getKey() + "-" + CounterLocalServiceUtil.increment();
					String storageType = imexStructure.getStorageType();
					int type = imexStructure.getStructureType();

					
					serviceContext.setUuid(imexStructure.getUuid());
					serviceContext.setAddGroupPermissions(true);
					serviceContext.setAddGuestPermissions(true);
					
					DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(imexStructure.getData());
					DDMFormLayout ddmFormLayout = _ddm.getDefaultDDMFormLayout(ddmForm);
					
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
							serviceContext);
					
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
	
	/*private DDMTemplate doImportTemplate(boolean debug, Group group, User user, Locale locale, File structureDir, DDMStructure structure) {
		
		DDMTemplate template = null;
		String templatefileBegin = FileNames.getTemplateFileNameBegin();
		File[] templateFiles = FileUtil.listFiles(structureDir, templatefileBegin);
		String groupName = GroupUtil.getGroupName(group, locale);
		
		if (templateFiles != null) {	
			
			for (File matchingTemplateFile : Arrays.asList(templateFiles)) {
				
				String templateFileName = matchingTemplateFile.getName();
				File templateFile = new File(structureDir, templateFileName);
				Date currentDate = new Date();
				
				try {
					
					ImExTemplate imexTemplate = (ImExTemplate)processor.read(ImExTemplate.class, templateFile);
					
					try {
					template = DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(imexTemplate.getUuid(), group.getGroupId());
					} catch (NoSuchTemplateException e) {
						
						long classNameId = ClassNameLocalServiceUtil.getClassNameId(DDMStructure.class);
						
						// New template creation
						template = DDMTemplateLocalServiceUtil.createDDMTemplate(CounterLocalServiceUtil.increment());
						template.setUuid(imexTemplate.getUuid());
						if (structure != null) {
							template.setClassPK(structure.getStructureId());
						}
						template.setCompanyId(group.getCompanyId());
						template.setGroupId(group.getGroupId());
						template.setUserId(user.getUserId());
						template.setTemplateKey("imex-" + imexTemplate.getKey() + "-" + CounterLocalServiceUtil.increment());
						template.setClassNameId(classNameId);
						template.setLanguage(imexTemplate.getLangType());
						template.setCreateDate(currentDate);
						template.setType(imexTemplate.getTemplateType());
						
					}
					// Template update logic
					template.setName(imexTemplate.getName());
					template.setScript(imexTemplate.getData());
					template.setModifiedDate(currentDate);
					
					DDMTemplateLocalServiceUtil.updateDDMTemplate(template);
					
					_log.info(MessageUtil.getOK(groupName, "[TEMPLATE : " + template.getName(locale) + "=> STRUCTURE : " + structure.getName(locale) + "]", templateFile));
					
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
	}*/
	

	private File getWCDDMImportDirectory(File companyDir) {
		
		File rolesDir = new File(companyDir, FileNames.DIR_WCDDM);
		
		if (rolesDir.exists()) {
			return rolesDir;
		} else {
			_log.warn("[" + rolesDir.getAbsolutePath() + "] does not exists");
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
