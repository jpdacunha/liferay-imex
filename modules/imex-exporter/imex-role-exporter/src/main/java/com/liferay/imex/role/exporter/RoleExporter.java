package com.liferay.imex.role.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.processor.ImexSerializer;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.role.exporter.xml.ImexRole;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;

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
			"imex.component.description=Role exporter",
			"service.ranking:Integer=10"
		},
		
		service = Exporter.class
	)
public class RoleExporter implements Exporter {
	
	private static final Log _log = LogFactoryUtil.getLog(RoleExporter.class);
	
	public static final String DIR_ROLE = "/role";
	public static final String FILENAME = "role.xml";
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected RoleLocalService roleLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexSerializer serializer;

	@Override
	public void doExport(Properties config, File destDir, long companyId) {
		
		_log.info(MessageUtil.getStartMessage("ROLE export process"));
		
		try {
			
			File rolesDir = initializeRolesExportDirectory( destDir);
			
			List<Role> roles = roleLocalService.getRoles(companyId);
			for (Role role : roles) {
				_log.info(MessageUtil.getMessage(role.getName(), 4));
				this.doRoleExport(role, rolesDir);
			}
			
		} catch (ImexException e) {
			_log.error(e,e);
			_log.error(MessageUtil.getErrorMessage(e)); 
		}
		
		_log.info(MessageUtil.getEndMessage("ROLE export process"));
		
	}
	
	private void doRoleExport(Role role, File rolesDir) throws ImexException {
		
		File roleDir = initializeSingleRoleExportDirectory(rolesDir, role);
		
		Locale locale = LocaleUtil.getDefault();
		String uuid = role.getUuid();
		String name = role.getName();
		int type = role.getType();
		String description = role.getDescription(locale);
		String friendlyURL = StringPool.BLANK;
	
		try {
			serializer.write(new ImexRole(uuid, name, type, description, friendlyURL), roleDir, getFileName());
		} catch (Exception e) {
			_log.error(e,e);
			throw new ImexException(e);
		}
		
		//TODO : JDA add rolePermission Export here
		
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
	 * Create root role directory (/role)
	 * @param exportDir
	 * @return
	 * @throws ImexException
	 */
	private File initializeRolesExportDirectory(File exportDir) throws ImexException {
		
		File rolesDir = new File(exportDir, getDirectoryName());
		boolean success = rolesDir.mkdirs();
		if (!success) {
			throw new ImexException("Failed to create directory " + rolesDir);
		}
		
		return rolesDir;
		
	}

	@Override
	public String getDirectoryName() {
		return DIR_ROLE;
	}

	@Override
	public String getProcessDescription() {
		return "Liferay Role Export Process";
	}

	@Override
	public String getFileName() {		
		return FILENAME;
	}

}
