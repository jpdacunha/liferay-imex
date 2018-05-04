package com.liferay.imex.role.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.ImexPropsUtil;
import com.liferay.imex.role.FileNames;
import com.liferay.imex.role.importer.configuration.ImExRoleImporterPropsKeys;
import com.liferay.imex.role.importer.service.ImportRolePermissionsService;
import com.liferay.imex.role.importer.service.ImportRoleService;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.Locale;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.execution.priority=10",	
			"imex.component.description=Roles importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class RoleImporter implements Importer {
	
	private static final String DESCRIPTION = "Liferay Role Import Process";

	private static final Log _log = LogFactoryUtil.getLog(RoleImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportRoleService importService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportRolePermissionsService importPermissionService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doImport(Bundle bundle, ServiceContext serviceContex, User user, Properties config, File companyDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "ROLE import process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExRoleImporterPropsKeys.IMPORT_ROLE_ENABLED));
		
		if (enabled) {
		
			try {
				
				File rolesDir = getRolesImportDirectory(companyDir);
				
				File[] rolesDirs = FileUtil.listFiles(rolesDir);
				for (File roleDir : rolesDirs) {
					this.doRoleImport(companyId, user, config, roleDir, debug);
				}
				
				
			} catch (Exception e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, "ROLE import");
		}
		
		reportService.getEndMessage(_log, "ROLE import process");
		
	}
	
	private void doRoleImport(long companyId, User user, Properties config, File roleDir, boolean debug) {
		
		if (roleDir != null) {
			
			if (roleDir.exists()) {
			
				String roleName = roleDir.getName();				
				String roleList = config.get(ImExRoleImporterPropsKeys.IMPORT_ROLES_IGNORE_LIST).toString();
				
				if (!ImexPropsUtil.contains(roleName, roleList)) {
					
					try {
						
						File roleFile = new File(roleDir, FileNames.ROLE_FILENAME + processor.getFileExtension());
						
						if (roleFile.exists()) {
							
							//Importing roles
							ImexRole imexRole = (ImexRole)processor.read(ImexRole.class, roleDir, FileNames.ROLE_FILENAME + processor.getFileExtension());
							importService.importRole(companyId, user, imexRole);
						
							//Importing roles permissions
							File rolePermissionFile = new File(roleDir, FileNames.ROLE_PERMISSION_FILENAME + processor.getFileExtension());
							
							if (rolePermissionFile.exists()) {
								boolean reInitPermissions = GetterUtil.getBoolean(config.get(ImExRoleImporterPropsKeys.IMPORT_ROLES_RESET_PERMISSIONS).toString());
								RolePermissions rolePermissions = (RolePermissions)processor.read(RolePermissions.class, roleDir, FileNames.ROLE_PERMISSION_FILENAME + processor.getFileExtension());
								importPermissionService.updateRolePermissions(companyId, imexRole, rolePermissions, reInitPermissions);
							} else {
								reportService.getDNE(_log, rolePermissionFile.getAbsolutePath());
							}
							
							reportService.getOK(_log, imexRole.getName());
							
						} else {
							reportService.getDNE(_log, roleFile.getAbsolutePath());
						}
						
						
					} catch (Exception e) {
						reportService.getError(_log, roleName, e.getMessage());
						if (debug) {
							_log.error(e,e);
						}
					}
					
				} else {
					reportService.getSkipped(_log, roleName);
				}
				
			} else {
				reportService.getDNE(_log, roleDir.getAbsolutePath());
			}
			
			
		} else {
			_log.error("Skipping null roleDir ...");
		}

	}

	private File getRolesImportDirectory(File companyDir) {
		
		File rolesDir = new File(companyDir, FileNames.DIR_ROLE);
		
		if (rolesDir.exists()) {
			return rolesDir;
		} else {
			reportService.getDNE(_log, rolesDir);
		}
		
		return null;
		
	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}

}
