package com.liferay.imex.role.importer.service.impl;

import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.imex.role.importer.service.ImportRoleService;
import com.liferay.imex.role.model.ImexRole;
import com.liferay.portal.kernel.exception.DuplicateRoleException;
import com.liferay.portal.kernel.exception.NoSuchRoleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

@Component
public class ImportRoleServiceImpl implements ImportRoleService {
	
	private static Log _log = LogFactoryUtil.getLog(ImportRoleServiceImpl.class);
	
	public Role importRole(long companyId, User user, ImexRole imexRole) throws PortalException {
	
		Role role = null;

		try {
		
			String uuid = imexRole.getUuid();
			if (uuid != null) {
				
				role = RoleLocalServiceUtil.getRoleByUuidAndCompanyId(uuid, companyId);
				//Role existe
				if (role != null) {
					
					String roleName = role.getName();
					//if role name is different of name defined in imex file then update roleName
					if ((roleName == null ) || (roleName != null && !roleName.equalsIgnoreCase(imexRole.getName()))){
						
						role.setName(imexRole.getName());
						RoleLocalServiceUtil.updateRole(role);
						_log.info(MessageUtil.getOK("[" + imexRole.getName() + ", uuid = " + role.getUuid() + " ]"));
						
					}
				}
				
			} else {
				
				role = RoleLocalServiceUtil.getRole(companyId, imexRole.getName());
			}

		} catch (NoSuchRoleException e) {
			
			_log.info(MessageUtil.getDNE(imexRole.getName()));
			
			//Initialisation des titres
			Map<java.util.Locale, String> titleMap = new HashMap<Locale, String>();
			initializeLocale(imexRole.getName(), titleMap);
			
			Map<java.util.Locale, String> descriptionMap = new HashMap<Locale, String>();
			initializeLocale(imexRole.getDescription(), descriptionMap);
			
			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setUuid(imexRole.getUuid());
			
			try {
				
				role = RoleLocalServiceUtil.addRole(user.getUserId(), null, 0, imexRole.getName(), titleMap, descriptionMap, imexRole.getRoleType().getIntValue(), null, serviceContext);
				_log.info(MessageUtil.getCreate(imexRole.getName()));
				
			} catch (DuplicateRoleException ex) { 
				
				//Update uuid in case of role already exists
				role = RoleLocalServiceUtil.getRole(companyId, imexRole.getName());
				role.setUuid(imexRole.getUuid());
				RoleLocalServiceUtil.updateRole(role);
				
				_log.info(MessageUtil.getUpdate(imexRole.getName() + ", uuid = " + role.getUuid()));
				
			}
			
		}
		
		return role;
		
		
	}
	
	private void initializeLocale(String value, Map<java.util.Locale, String> map) {
		
		Set<Locale> availableLocales = LanguageUtil.getAvailableLocales();
		
		for (Locale locale : availableLocales) {
			map.put(locale, value);
		}
		
	}

}
