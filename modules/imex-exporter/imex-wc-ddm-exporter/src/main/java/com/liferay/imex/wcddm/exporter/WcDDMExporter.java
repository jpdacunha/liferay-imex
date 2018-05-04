package com.liferay.imex.wcddm.exporter;


import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
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
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
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
			"imex.component.execution.priority=20",
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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMTemplateLocalService dDMTemplateLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMStructureLocalService dDMStructureLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ClassNameLocalService classNameLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doExport(Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "WEBCONTENT export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmExporterPropsKeys.EXPORT_WCDDM_ENABLED));
		
		if (enabled) {
		
			try {
				
				Company company = companyLocalService.getCompany(companyId);
				
				File wcDdmDir = initializeWCDDMExportDirectory(destDir);
				
				List<Group> groups = groupLocalService.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
				
				for (Group group : groups) {

					boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
					if (isSite) {
						
						reportService.getStartMessage(_log, group, locale);
						doExport(config, group, wcDdmDir, locale, debug);
						reportService.getEndMessage(_log, group, locale);
						
					}

				}

				// Global Scope Export
				Group companyGroup = company.getGroup();
				reportService.getStartMessage(_log, companyGroup, locale);
				doExport(config, companyGroup, wcDdmDir, locale, debug);
				reportService.getEndMessage(_log, companyGroup, locale);
				
			} catch (ImexException e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			} catch (PortalException e) {
				_log.error(e,e);
			}
			
		} else {
			reportService.getDisabled(_log, "WEBCONTENT export");
		}
		
		reportService.getEndMessage(_log, "WEBCONTENT export process");
		
	}
	
	public void doExport(Properties config, Group group, File groupsDir, Locale locale, boolean debug) throws ImexException {
		
		if (group != null) {
			
			//String groupName = GroupUtil.getGroupName(group, locale);
			
			long classNameId = classNameLocalService.getClassNameId(JournalArticle.class);					
			List<DDMStructure> ddmStructures = dDMStructureLocalService.getStructures(group.getGroupId(), classNameId);
			
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
										
										String groupName = GroupUtil.getGroupName(group, locale);
										reportService.getOK(_log, groupName, "STRUCTURE : "  + ddmStructure.getName(locale));
										
										List<DDMTemplate> ddmTemplates = dDMTemplateLocalService.getTemplatesByClassPK(ddmStructure.getGroupId(), ddmStructure.getPrimaryKey());
										
										//Iterate over templates
										for(DDMTemplate ddmTemplate : ddmTemplates){
											processor.write(new ImExTemplate(ddmTemplate), structureDir, FileNames.getTemplateFileName(ddmTemplate, group, locale, processor.getFileExtension()));
											reportService.getOK(_log, groupName, "TEMPLATE : "  + ddmTemplate.getName(locale));
										}
										reportService.getSeparator(_log);
										
									} catch (Exception e) {
										reportService.getError(_log, ddmStructure.getName(locale), e.getMessage());
										if (debug) {
											_log.error(e,e);
										}
									}
							
								} else {
									reportService.getDNE(_log, structureDir.getAbsolutePath());
								}
								
							} else {
								_log.error("structureDir is null ...");						
							}
							
						}
						
					} else {
						reportService.getDNE(_log, groupDir.getAbsolutePath());
					}
					
				} else {
					_log.error("groupDir is null ...");
				}
				
			} else {
				reportService.getEmpty(_log, group, locale);				
			}
			
		} else {
			_log.error("Skipping null group ...");
		}
		
	}
	
	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
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
