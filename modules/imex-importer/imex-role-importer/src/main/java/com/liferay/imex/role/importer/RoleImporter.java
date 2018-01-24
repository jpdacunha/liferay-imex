package com.liferay.imex.role.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.configuration.ImexPropsUtil;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.role.FileNames;
import com.liferay.imex.role.importer.configuration.ImExRoleImporterPropsKeys;
import com.liferay.imex.role.importer.service.ImportRolePermissionsService;
import com.liferay.imex.role.importer.service.ImportRoleService;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		property = {
			"imex.component.description=Roles importer",
			"service.ranking:Integer=10"
		},
		service = Importer.class
	)
public class RoleImporter implements Importer {
	
	private static final Log _log = LogFactoryUtil.getLog(RoleImporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportRoleService importService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImportRolePermissionsService importPermissionService;

	@Override
	public void doImport(User user, Properties config, File companyDir, long companyId, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("ROLE import process"));
		
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
				_log.error(MessageUtil.getErrorMessage(e)); 
			}
			
		} else {
			_log.info(MessageUtil.getDisabled("ROLE import"));
		}
		
		_log.info(MessageUtil.getEndMessage("ROLE import process"));
		
	}
	
	private void doRoleImport(long companyId, User user, Properties config, File roleDir, boolean debug) {
		
		if (roleDir != null) {
			
			if (roleDir.exists()) {
			
				String roleName = roleDir.getName();				
				String roleList = config.get(ImExRoleImporterPropsKeys.IMPORT_ROLES_IGNORE_LIST).toString();
				
				if (!ImexPropsUtil.contains(roleName, roleList)) {
					
					try {
						
						if (roleDir != null) {
							
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
									_log.warn(MessageUtil.getDNE(rolePermissionFile.getAbsolutePath()));
								}
								
								_log.info(MessageUtil.getOK(imexRole.getName()));
								
							} else {
								_log.warn(MessageUtil.getDNE(roleFile.getAbsolutePath()));
							}
							
							//Fixme JDA : Update roles and permissions here
						}
						
					} catch (Exception e) {
						_log.error(MessageUtil.getError(roleName, e.getMessage()));
						if (debug) {
							_log.error(e,e);
						}
					}
					
				} else {
					_log.info(MessageUtil.getSkipped(roleName));
				}
				
			} else {
				_log.warn(MessageUtil.getDNE(roleDir.getAbsolutePath()));
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
			_log.warn("[" + rolesDir.getAbsolutePath() + "] does not exists");
		}
		
		return null;
		
	}

	@Override
	public String getProcessDescription() {
		return "Liferay Role Import Process";
	}

}
