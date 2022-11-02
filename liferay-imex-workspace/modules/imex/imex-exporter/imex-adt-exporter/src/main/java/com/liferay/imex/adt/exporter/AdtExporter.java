package com.liferay.imex.adt.exporter;


import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.imex.adt.FileNames;
import com.liferay.imex.adt.exporter.configuration.ImExWCDDmExporterPropsKeys;
import com.liferay.imex.adt.model.ImExAdt;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.model.ExporterRawContent;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.GroupUtil;
import com.liferay.imex.core.util.statics.ImexNormalizer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.Arrays;
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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected DDMTemplateLocalService dDMTemplateLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected CompanyLocalService companyLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ClassNameLocalService classNameLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doExport(User user, Properties config, File adtDir, long companyId, Locale locale, List<ExporterRawContent> rawContentToExport, boolean debug) {
		
		reportService.getStartMessage(_log, "ADT export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExWCDDmExporterPropsKeys.EXPORT_ADT_ENABLED));
		
		if (enabled) {
			
			String stringList = GetterUtil.getString(config.get(ImExWCDDmExporterPropsKeys.EXPORT_ADT_TYPES_LIST));
			List<String> types = CollectionUtil.getList(stringList);
			
			if (types != null && types.size() > 0) {
				
				reportService.getMessage(_log, "Registered types to export : [" + Arrays.toString(types.toArray()) + "]");
				
				try {
					
					List<Group> groups = groupLocalService.getCompanyGroups(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
					
					for (Group group : groups) {
								
						boolean isSite = group.isSite() && !group.getFriendlyURL().equals("/control_panel");
						if (isSite) {
							reportService.getStartMessage(_log, group, locale);
							for (String classType : types) {
								doExport(config, group, adtDir, locale, debug, classType, rawContentToExport);	
							}
							reportService.getEndMessage(_log, group, locale);
						} else {
							reportService.getSkipped(_log, "group : " + GroupUtil.getGroupName(group, locale));
						}

					}
					
				} catch (ImexException e) {
					_log.error(e,e);
					reportService.getError(_log, e);
				}
				
			} else {
				reportService.getMessage(_log, "No configured types to export");
			}
			
		} else {
			reportService.getDisabled(_log, DESCRIPTION);
		}
		
		reportService.getEndMessage(_log, "ADT export process");
		
	}
	
	public void doExport(Properties config, Group group, File groupsDir, Locale locale, boolean debug, String classType, List<ExporterRawContent> rawContentToExport) throws ImexException {
		
		if (group != null) {
			
			long classNameId = classNameLocalService.getClassNameId(classType);			
			List<DDMTemplate> adts = dDMTemplateLocalService.getTemplates(group.getGroupId(), classNameId);
			
			if (adts != null && adts.size() > 0) {
			
				File groupDir = initializeSingleGroupDirectory(groupsDir, group);
				
				if (groupDir != null) {
					
					if (groupDir.exists()) {
						
						//Iterate over templates (ADT)
						for(DDMTemplate ddmTemplate : adts){
												
							File adtDir = initializeSingleAdtExportDirectory(groupDir, classType);
							
							if (adtDir != null) {
								
								if (adtDir.exists()) {
							
									try {
										
										String groupName = GroupUtil.getGroupName(group, locale);
										
										ImExAdt imexAdt = new ImExAdt(ddmTemplate, classType);
										
										processor.write(imexAdt, adtDir, FileNames.getAdtFileName(ddmTemplate, locale, processor.getFileExtension()));
										
										String rawFileName = FileNames.getGroupAdtFileName(ddmTemplate, group, locale, StringPool.PERIOD + imexAdt.getLangType());
										rawContentToExport.add(new ExporterRawContent(rawFileName, imexAdt.getData()));
										
					
										reportService.getOK(_log, groupName, "ADT : "  + ddmTemplate.getName(locale) + ", type : " + classType);
										
										
									} catch (Exception e) {
										reportService.getError(_log, ddmTemplate.getName(locale), e);
										if (debug) {
											_log.error(e,e);
										}
									}
							
								} else {
									reportService.getDNE(_log, adtDir.getAbsolutePath());
								}
								
							} else {
								_log.error("adtDir is null ...");						
							}
							
						}
						
					} else {
						reportService.getDNE(_log, groupDir.getAbsolutePath());
					}
					
				} else {
					_log.error("groupDir is null ...");
				}
				
			} else {
				reportService.getEmpty(_log, group, locale, classType);				
			}
			
		} else {
			_log.error("Skipping null group ...");
		}
		
	}
	
	@Override
	public void deploy() {
		
	}

	@Override
	public void undeploy() {
		
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
		
		String name = GroupUtil.getGroupFriendlyUrlAsName(group);
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
	 * Return root directory name
	 *
	 */
	@Override
	public String getRootDirectoryName() {
		
		return FileNames.DIR_ADT;

	}

}
