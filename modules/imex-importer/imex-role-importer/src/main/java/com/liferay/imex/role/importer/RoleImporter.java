package com.liferay.imex.role.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.role.FileNames;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.RoleLocalService;

import java.io.File;
import java.util.List;
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
	protected RoleLocalService roleLocalService;

	@Override
	public void doImport(Properties config, File companyDir, long companyId, boolean debug) {
		
		_log.info(MessageUtil.getStartMessage("ROLE import process"));
		
		//FIXME : manage import.role.enabled parameter
		
		try {
			
			File rolesDir = getRolesImportDirectory(companyDir);
			
			List<Role> roles = roleLocalService.getRoles(companyId);
			for (Role role : roles) {
				this.doRoleImport(config, role, rolesDir, debug);
			}
			
			
		} catch (Exception e) {
			_log.error(e,e);
			_log.error(MessageUtil.getErrorMessage(e)); 
		}
		
		_log.info(MessageUtil.getEndMessage("ROLE import process"));
		
	}
	
	private void doRoleImport(Properties config, Role role, File rolesDir, boolean debug) {
		
		if (role != null) {
			
			File roleDir = getSingleRoleImportDirectory(rolesDir, role);
					
			try {
				
				if (roleDir != null) {
					
					File roleFile = new File(roleDir, FileNames.ROLE_FILENAME + processor.getFileExtension());
					
					if (roleFile.exists()) {
						ImexRole imexRole = (ImexRole)processor.read(ImexRole.class, roleDir, FileNames.ROLE_FILENAME + processor.getFileExtension());
						_log.info(MessageUtil.getOK(imexRole.getName()));
					} else {
						_log.warn("[" + roleFile.getAbsolutePath() + "] does not exists");
					}
					
					//Update roles and permissions here
				}
				
			} catch (Exception e) {
				_log.error(MessageUtil.getError(role.getName(), e.getMessage()));
				if (debug) {
				_log.error(e,e);
				}
			}
			
			
		} else {
			_log.error("Skipping null role ...");
		}

	}

	private File getSingleRoleImportDirectory(File rolesDir, Role role) {
		
		String roleDirName = role.getName();
		File roleDir = new File(rolesDir, roleDirName);
		if (roleDir.exists()) {
			return roleDir;
		} else {
			_log.warn("[" + roleDir.getAbsolutePath() + "] does not exists");
		}
		
		return null;
		
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
