package com.liferay.imex.role.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.ImexPropsUtil;
import com.liferay.imex.role.FileNames;
import com.liferay.imex.role.exporter.configuration.ImExRoleExporterPropsKeys;
import com.liferay.imex.role.exporter.service.ExportRolePermissionsService;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.imex.role.model.RolePermissions;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

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
			"imex.component.execution.priority=10",
			"imex.component.description=Role exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class RoleExporter implements Exporter {
	
	public static final String DESCRIPTION = "Liferay Role Export Process";
	
	private static final Log _log = LogFactoryUtil.getLog(RoleExporter.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexProcessor processor;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ExportRolePermissionsService rolePermissionServive;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public void doExport(User user, Properties config, File rolesDir, long companyId, Locale locale, boolean debug) {
		
		reportService.getStartMessage(_log, "ROLE export process");
		
		boolean enabled = GetterUtil.getBoolean(config.get(ImExRoleExporterPropsKeys.EXPORT_ROLE_ENABLED));
		
		if (enabled) {
		
			try {
					
				List<Role> roles = roleLocalService.getRoles(companyId);
				for (Role role : roles) {
					this.doRoleExport(config, role, rolesDir, debug);
				}
				
			} catch (ImexException e) {
				_log.error(e,e);
				reportService.getError(_log, e); 
			}
			
		} else {
			reportService.getDisabled(_log, "ROLE export");
		}
		
		reportService.getEndMessage(_log, "ROLE export process");
		
	}
	
	private void doRoleExport(Properties config, Role role, File rolesDir, boolean debug) throws ImexException {
				
		if (role != null) {
			
			Locale locale = LocaleUtil.getDefault();
			String uuid = role.getUuid();
			String name = role.getName();
			int type = role.getType();
			String description = role.getDescription(locale);
			String friendlyURL = StringPool.BLANK;
			long companyId = role.getCompanyId();
			long roleId = role.getRoleId();
			
			//No null control here because this param cannot be null
			String roleList = config.get(ImExRoleExporterPropsKeys.EXPORT_ROLES_IGNORE_LIST).toString();

			if (!ImexPropsUtil.contains(role.getName(), roleList)) {
				
				File roleDir = initializeSingleRoleExportDirectory(rolesDir, role);
				
				if (roleDir != null) {
									
					if (roleDir.exists()) {
					
						try {
							
							//Writing role file
							processor.write(new ImexRole(uuid, name, type, description, friendlyURL), roleDir, FileNames.ROLE_FILENAME + processor.getFileExtension());
						
							//Writing role permission file
							RolePermissions source =  rolePermissionServive.getRolePermissions(companyId, roleId);
							processor.write(source, roleDir, FileNames.ROLE_PERMISSION_FILENAME + processor.getFileExtension());
							
							reportService.getOK(_log, role.getName());
						
						} catch (Exception e) {
							reportService.getError(_log, role.getName(), e);
							if (debug) {
								_log.error(e,e);
							}
						}
						
					} else {
						reportService.getDNE(_log, roleDir.getAbsolutePath());
					}
					
				} else {
					_log.error("roleDir is null ...");
				}
				
			} else {
				reportService.getSkipped(_log, role.getName());
			}
			
		} else {
			_log.error("Skipping null role ...");
		}
		
	}

	/**
	 * Create directory for single role (/role-name)
	 * @param roleDir
	 * @param role
	 * @return
	 * @throws ImexException
	 */
	private File initializeSingleRoleExportDirectory(File rolesDir, Role role) throws ImexException {
		
		String roleDirName = role.getName();
		File roleDir = new File(rolesDir, roleDirName);
		boolean success = roleDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + roleDir);
		}
		
		return roleDir;
		
	}

	/**
	 * Return root directory name
	 *
	 */
	@Override
	public String getExporterRootDirectory() {
		
		return FileNames.DIR_ROLE;

	}

	@Override
	public String getProcessDescription() {
		return DESCRIPTION;
	}
	
}
