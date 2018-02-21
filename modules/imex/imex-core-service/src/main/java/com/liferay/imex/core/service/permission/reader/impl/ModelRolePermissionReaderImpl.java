package com.liferay.imex.core.service.permission.reader.impl;

import com.liferay.imex.core.service.permission.model.ModelPermissionBatch;
import com.liferay.imex.core.service.permission.model.ModelRolePermissionBatch;
import com.liferay.imex.core.service.permission.reader.ModelRolePermissionReader;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;

@Component
public class ModelRolePermissionReaderImpl implements ModelRolePermissionReader {
	
	private static Log _log = LogFactoryUtil.getLog(ModelRolePermissionReaderImpl.class);
	
	//Cache interne de gestion
	private final static ConcurrentHashMap<String, List<ModelRolePermissionBatch>> batchs = new ConcurrentHashMap<String, List<ModelRolePermissionBatch>>();
	
	//Concrete type instanciation
	private ModelRolePermissionReader reader = new PropertiesModelRolePermissionReaderImpl();
	
	/*
	 * (non-Javadoc)
	 * @see com.liferay.portal.permission.model.batch.reader.ModelRolePermissionReader#getRolesBatchs(java.lang.String)
	 */
	public List<ModelRolePermissionBatch> getRolesBatchs(String batchId) throws PortalException, SystemException {
		
		List<ModelRolePermissionBatch> liste = null;
		
		if (batchId != null && !batchId.equals("")) {
			
			if (!batchs.contains(batchId)) {
				
				liste = reader.getRolesBatchs(batchId);
				
				if (liste != null && liste.size() > 0) {
					batchs.put(batchId, liste);
				}
				
			} 
			
			liste = batchs.get(batchId);
			
		} else {
			_log.warn("Invalid null parameter : batchId");
		}
		
		if (liste == null || liste.size() == 0) {
			if (!batchId.equals(ModelPermissionBatch.NO_PERMISSIONS_LAYOUT)) {
				_log.error("No rules defined for [" + batchId + "]");
			}
			liste = new ArrayList<ModelRolePermissionBatch>();
		}
		
		return liste;
		
	}

	@Override
	public boolean isReinitOnset(String batchId) {
		return reader.isReinitOnset(batchId);
	}

}
