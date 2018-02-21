package com.liferay.imex.adt.exporter;


import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.imex.adt.FileNames;
import com.liferay.imex.adt.exporter.configuration.ImExWCDDmExporterPropsKeys;
import com.liferay.imex.adt.model.ImExAdt;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.imex.core.util.statics.MessageUtil;
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
			"imex.component.execution.priority=100",
			"imex.component.description=ADT exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class AdtExporter implements Exporter {
	
	public static final String DESCRIPTION = "ADT exporter";
	
	private static final Log _log = LogFactoryUtil.getLog(AdtExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;

	@Override
	public void doExport(Properties config, File destDir, long companyId, Locale locale, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("ADT export process"));
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmExporterPropsKeys.EXPORT_ADT_ENABLED));
		
		if (enabled) {
			
			String stringList = GetterUtil.getString(config.get(ImExWCDDmExporterPropsKeys.EXPORT_ADT_TYPES_LIST));
			List<String> types = CollectionUtil.getArray(stringList);
			
			if (types != null && types.size() > 0) {
				
				try {
					
					Company company = CompanyLocalServiceUtil.getCompany(companyId);
					
					File adtDir = initializeAdtExportDirectory(destDir);
					
					List<Group> groups = GroupLocalServiceUtil.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
					for (Group group : groups) {
						
						for (String classType : types) {
							
							boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
							if (isSite) {
								
								_log.info(MessageUtil.getStartMessage(group, locale));
								doExport(config, group, adtDir, locale, debug, classType);
								_log.info(MessageUtil.getEndMessage(group, locale));
								
							}
							
						}
						

					}

					// Global Scope Export
					Group companyGroup = company.getGroup();
					_log.info(MessageUtil.getStartMessage(companyGroup, locale));

					for (String classType : types) {			
						doExport(config, companyGroup, adtDir, locale, debug, classType);	
					}
					
					_log.info(MessageUtil.getEndMessage(companyGroup, locale));
					
				} catch (ImexException e) {
					_log.error(e,e);
					_log.error(MessageUtil.getErrorMessage(e)); 
				} catch (PortalException e) {
					_log.error(e,e);
				}
				
			} else {
				_log.info(MessageUtil.getMessage("No configured types to export"));
			}
			
		} else {
			_log.info(MessageUtil.getDisabled(DESCRIPTION));
		}
		
		_log.info(MessageUtil.getEndMessage("ADT export process"));
		
	}
	
	public void doExport(Properties config, Group group, File groupsDir, Locale locale, boolean debug, String classType) throws ImexException {
		
		if (group != null) {
			
			long classNameId = ClassNameLocalServiceUtil.getClassNameId(classType);			
			List<DDMTemplate> adts = DDMTemplateLocalServiceUtil.getTemplates(group.getGroupId(), classNameId);
			
			if (adts != null && adts.size() > 0) {
			
				File groupDir = initializeSingleGroupDirectory(groupsDir, group);
				
				if (groupDir != null) {
					
					if (groupDir.exists()) {
				
						//Iterate over structures
						for(DDMTemplate ddmTemplate : adts){
												
							File adtDir = initializeSingleAdtExportDirectory(groupDir, classType);
							
							if (adtDir != null) {
								
								if (adtDir.exists()) {
							
									try {
										
										String groupName = GroupUtil.getGroupName(group, locale);
										
										processor.write(new ImExAdt(ddmTemplate, classType), adtDir, FileNames.getAdtFileName(ddmTemplate, group, locale, processor.getFileExtension()));
										_log.info(MessageUtil.getOK(groupName, "ADT : "  + ddmTemplate.getName(locale)));
										
									} catch (Exception e) {
										_log.error(MessageUtil.getError(ddmTemplate.getName(locale), e.getMessage()));
										if (debug) {
											_log.error(e,e);
										}
									}
							
								} else {
									_log.warn(MessageUtil.getDNE(adtDir.getAbsolutePath()));
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
				_log.info(MessageUtil.getEmpty(group, locale));				
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
	 * Create directory for single item
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleAdtExportDirectory(File groupDir, String classType) throws ImexException {
		
		String name = ImexNormalizer.convertToKey(classType);
		File dir = new File(groupDir, name);
		dir.mkdirs();		
		return dir;
		
	}


	/**
	 * Create root directory
	 * @param exportDir
	 * @return
	 * @throws ImexException
	 */
	private File initializeAdtExportDirectory(File exportDir) throws ImexException {
		
		File rolesDir = new File(exportDir, FileNames.DIR_ADT);
		boolean success = rolesDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + rolesDir);
		}
		
		return rolesDir;
		
	}

}
