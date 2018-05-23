package com.liferay.imex.core.api.permission.model;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Resource;

import java.util.List;

/**
 * 
 * Classe representant un batch de permissions a positionner pour une resource 
 * @author jpdacunha
 * 
 */
public class ModelPermissionBatch {
	
	private static Log _log = LogFactoryUtil.getLog(ModelPermissionBatch.class);

	private Resource resource;
	private String batchId;
	private boolean reInitOnSet;
	private List<ModelRolePermissionBatch> batchs;

	public boolean isReInitOnSet() {
		return reInitOnSet;
	}

	public List<ModelRolePermissionBatch> getBatchs() {
		return batchs;
	}

	// Constructeur prive inutilisable
	@SuppressWarnings("unused")
	private ModelPermissionBatch() {
		super();
	}

	public ModelPermissionBatch(Resource resource, String batchId, boolean reinitOnset, List<ModelRolePermissionBatch> batchs) {
		this.resource = resource;
		this.batchId = batchId;
		this.reInitOnSet = reinitOnset;
		this.batchs = batchs;
	}
	
	/**
	 * Effectue la validation du batch a positionner pour des rôles sur une ressource
	 * @param batch
	 * @return
	 * @throws SystemException
	 */
	public static boolean validate(ModelPermissionBatch batch) throws SystemException {
		
		boolean valid = false;
		
		if (batch != null) {
			
			Resource resource = batch.getResource();
			return (resource != null);
			
		} else {
			_log.warn("Unable to execute batch : mandatory parameter batch is null");
		}
		
		return valid;
		
	}
	
	/**
	 * Retourne la clee primaire d'une ressource. Retour 0 en cas de probleme.
	 * @param resource
	 * @return
	 */
	public static long getPrimaryKey(Resource resource) {
		
		//Recupere la page courante
		long primaryKey = 0;
		boolean displayWarn = false;
		
		if (resource != null) {
			
			String primKey = resource.getPrimKey();
			
			try {
				if (primKey != null && !primKey.equals("")) {
					primaryKey = Long.valueOf(primKey);
				} else {
					displayWarn = true;
				}
			} catch (Exception e) {
				displayWarn = true;
			}
			
			if (displayWarn) {
				_log.warn("Invalid primary key for resource id=[" + resource.getResourceId() + "]");
			}
			
		} else {
			_log.warn("Resource is null");
		}
		return primaryKey;
		
	}
	
	public String getBatchId() {
		return batchId;
	}
	
	public Resource getResource() {
		return resource;
	}
	
}
