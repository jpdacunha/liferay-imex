package com.liferay.imex.core.service.configuration.impl;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.permission.ImexModelRolePermissionReader;
import com.liferay.imex.core.api.permission.model.ModelRolePermissionBatch;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;

@Component
public class ImexPropertiesModelRolePermissionReaderImpl implements ImexModelRolePermissionReader {
	
	private static Log _log = LogFactoryUtil.getLog(ImexPropertiesModelRolePermissionReaderImpl.class);

	private final static boolean REINIT_DEFAULT_VALUE = true;
	
	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.permission.model.batch.reader.ModelRolePermissionReader#getRolesBatchs(java.lang.String)
	 */
	public List<ModelRolePermissionBatch> getRolesBatchs(String batchId, Properties props) throws PortalException, SystemException {
		
		List<ModelRolePermissionBatch> liste = new ArrayList<ModelRolePermissionBatch>();
		
		if (props == null) {
			_log.warn("Missing required properties");
			return liste;
		}
					
		String[] roles = CollectionUtil.getArray(props.getProperty(ImExCorePropsKeys.PERMISSIONS_SUPPORTED_ROLES));
		
		if (roles != null  && roles.length > 0) {
			
			List<String> rolesNames = Arrays.asList(roles);
			
			for (String roleName : rolesNames) {
				
				ModelRolePermissionBatch batch = readRoleBatch(batchId, roleName, props);
				if (ModelRolePermissionBatch.validate(batch)) {
					liste.add(batch);
				}
				
			}
			
		} else {
			_log.error("Invalid configuration : no roles are defined. Please check  [" + ImExCorePropsKeys.PERMISSIONS_SUPPORTED_ROLES + "] is correctly defined");
		}
		
		return liste;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.permission.service.ModelRolePermissionReader#isReinitOnset(java.lang.String)
	 */
	public boolean isReinitOnset(String batchId, Properties props) {
		
		String key = buildReInitKey(batchId);
		
		if (key != null) {
			return GetterUtil.getBoolean(props.getProperty(key), REINIT_DEFAULT_VALUE);
		}
		
		return REINIT_DEFAULT_VALUE;
		
	}
	
	/**
	 * Lis dans le fichier de configuration le batch configuré pour un role. Null sinon
	 * @param batchId
	 * @param roleName
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private static ModelRolePermissionBatch readRoleBatch(String batchId, String roleName, Properties properties) throws PortalException, SystemException {
		
		List<String> actions = null;
		ModelRolePermissionBatch batch = null;
		String key = buildRoleActionsKey(batchId, roleName);
		
		if (key != null) {
				
			String[] actionsArray = CollectionUtil.getArray((String)properties.get(key));
			
			if (actionsArray != null  && actionsArray.length > 0) {
				
				actions = Arrays.asList(actionsArray);
				
				batch = new ModelRolePermissionBatch(roleName, actions);
				
			} else {
				_log.warn("No rule defined for role:[" + roleName + "], key:[" + key + "]");
			}
				
		}
		
		if (ModelRolePermissionBatch.validate(batch)) {
			return batch;
		}
		 
		return null;
		
	}
	
	private static String buildReInitKey(String batchId) {
		
		StringBuilder key = null;
		if (batchId != null && !batchId.equals("")) {
			
			key = new StringBuilder();
			key.append(ImExCorePropsKeys.RULE_PREFIX_KEY);
			key.append(StringPool.PERIOD);
			key.append(batchId);
			key.append(StringPool.PERIOD);
			key.append(ImExCorePropsKeys.REINIT_SUFIX_KEY);
			
		}
		
		if (key == null) {
			return null;
		}
		return key.toString();
		
	}
	
	private static String buildRoleActionsKey(String batchId, String roleName) {
		
		StringBuilder key = null;
		if (roleName != null && !roleName.equals("")) {
			
			//Traitement des rôles avec espaces
			if (roleName.contains(" ")) {
				roleName = roleName.replaceAll(" ", "");
			}
			
			roleName = roleName.toUpperCase();
			
			if (batchId != null && !batchId.equals("")) {
				
				key = new StringBuilder();
				key.append(ImExCorePropsKeys.RULE_PREFIX_KEY);
				key.append(StringPool.PERIOD);
				key.append(batchId);
				key.append(StringPool.PERIOD);
				key.append(roleName);
				key.append(StringPool.PERIOD);
				key.append(ImExCorePropsKeys.RULE_SUFIX_KEY);
				
			}
			
		}
		
		if (key == null) {
			return null;
		}
		return key.toString();
		
	}

}
