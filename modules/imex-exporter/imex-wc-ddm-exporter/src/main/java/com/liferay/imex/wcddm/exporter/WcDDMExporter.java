package com.liferay.imex.wcddm.exporter;


import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.wcddm.FileNames;
import com.liferay.imex.wcddm.exporter.configuration.ImExWCDDmExporterPropsKeys;
import com.liferay.imex.wcddm.model.ImExStructure;
import com.liferay.imex.wcddm.model.ImExTemplate;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
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
			"imex.component.description=Web Content DDM exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class WcDDMExporter implements Exporter {
	
	public static final String DESCRIPTION = "Web Content DDM exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(WcDDMExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;

	@Override
	public void doExport(Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("WEBCONTENT export process"));
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmExporterPropsKeys.EXPORT_WCDDM_ENABLED));
		
		if (enabled) {
		
			try {
				
				Company company = CompanyLocalServiceUtil.getCompany(companyId);
				
				File wcDdmDir = initializeWCDDMExportDirectory(destDir);
				
				List<Group> groups = GroupLocalServiceUtil.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
				
				for (Group group : groups) {

					boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
					if (isSite) {
						doExport(config, group, wcDdmDir, locale, debug);
					}

				}

				// Global Scope Export
				doExport(config, company.getGroup(), wcDdmDir, locale, debug);
				
			} catch (ImexException e) {
				_log.error(e,e);
				_log.error(MessageUtil.getErrorMessage(e)); 
			} catch (PortalException e) {
				_log.error(e,e);
			}
			
		} else {
			_log.info(MessageUtil.getDisabled("WEBCONTENT export"));
		}
		
		_log.info(MessageUtil.getEndMessage("WEBCONTENT export process"));
		
	}
	
	public void doExport(Properties config, Group group, File groupsDir, Locale locale, boolean debug) throws ImexException {
		
		if (group != null) {
			
			String groupName = GroupUtil.getGroupName(group, locale);
			
			long classNameId = ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class);					
			List<DDMStructure> ddmStructures = DDMStructureLocalServiceUtil.getStructures(group.getGroupId(), classNameId);
			
			if (ddmStructures != null && ddmStructures.size() > 0) {
			
				File groupDir = initializeSingleGroupDirectory(groupsDir, group);
				
				if (groupDir != null) {
					
					if (groupDir.exists()) {
				
						//Iterate over structures
						for(DDMStructure ddmStructure : ddmStructures){
												
							File structureDir = initializeSingleWCDDMExportDirectory(groupDir, ddmStructure);
							
							if (structureDir != null) {
								
								if (structureDir.exists()) {
							
									try {
										
										processor.write(new ImExStructure(ddmStructure), structureDir, FileNames.getStructureFileName(ddmStructure, group, locale, processor.getFileExtension()));
										_log.info(MessageUtil.getOK(groupName, ddmStructure.getName(locale)));
										
										List<DDMTemplate> ddmTemplates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(ddmStructure.getGroupId(), ddmStructure.getPrimaryKey());
										
										//Iterate over templates
										for(DDMTemplate ddmTemplate : ddmTemplates){
											processor.write(new ImExTemplate(ddmTemplate), structureDir, FileNames.getTemplateFileName(ddmTemplate, group, locale, processor.getFileExtension()));
											_log.info(MessageUtil.getOK(groupName, ddmStructure.getName(locale)));
										}
										
									} catch (Exception e) {
										_log.error(MessageUtil.getError(ddmStructure.getName(locale), e.getMessage()));
										if (debug) {
										_log.error(e,e);
										}
									}
							
								} else {
									_log.warn(MessageUtil.getDNE(structureDir.getAbsolutePath()));
								}
								
							} else {
								_log.error("structureDir is null ...");						
							}
							
						}
						
					} else {
						_log.warn(MessageUtil.getDNE(groupDir.getAbsolutePath()));
					}
					
				} else {
					_log.error("groupDir is null ...");
				}
				
			} else {
				_log.info(MessageUtil.getEmpty(groupName));				
			}
			
		} else {
			_log.error("Skipping null group ...");
		}
		
	}
	
	@Override
	public String getProcessDescription() {
		return "Web Content DDM export";
	}
	
	/**
	 * Create directory for single role (/role-name)
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleGroupDirectory(File groupsDir, Group group) throws ImexException {
		
		String name = group.getFriendlyURL();
		if (group.isCompany()) {
			name = GroupUtil.GLOBAL;
		}
		
		name = ImexNormalizer.getDirNameByFriendlyURL(name);
		File dir = new File(groupsDir, name);
		dir.mkdirs();		
		return dir;
		
	}
	
	/**
	 * Create directory for single role (/role-name)
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleWCDDMExportDirectory(File groupDir, DDMStructure structure) throws ImexException {
		
		String name = ImexNormalizer.convertToKey(structure.getStructureKey());
		File dir = new File(groupDir, name);
		dir.mkdirs();		
		return dir;
		
	}


	/**
	 * Create root role directory (/role)
	 * @param exportDir
	 * @return
	 * @throws ImexException
	 */
	private File initializeWCDDMExportDirectory(File exportDir) throws ImexException {
		
		File rolesDir = new File(exportDir, FileNames.DIR_WCDDM);
		boolean success = rolesDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + rolesDir);
		}
		
		return rolesDir;
		
	}

}
