package com.liferay.imex.core.api.permission.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.List;

public class ModelRolePermissionBatch {
	
	private static Log _log = LogFactoryUtil.getLog(ModelRolePermissionBatch.class);
	
	private String roleName;
	private List<String> actionIds = new ArrayList<String>();
	
	public ModelRolePermissionBatch(String role, List<String> actionIds) {
		super();
		this.roleName = role;
		this.actionIds = actionIds;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public List<String> getActionIds() {
		return actionIds;
	}
	
	/**
	 * Validate role batch
	 * @param batch
	 * @return
	 */
	public static boolean validate(ModelRolePermissionBatch batch) {
		
		boolean valid = false;
		
		if (batch != null) {
			
			String roleName = batch.getRoleName();
			if (roleName != null && !roleName.equals("")) {
				
				if (batch.getActionIds() != null && batch.getActionIds().size() > 0) {
					valid = true;
				}
				
			}
			
		} else {
			_log.debug("Invalid parameter : execution aborted ...");
		}
		
		return valid;
		
	}

}
