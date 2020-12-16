package com.liferay.imex.core.api.lar;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

public interface ImexLarService {
	
	public void doImport(long userId, Group group, File groupDir, File larFile) throws SystemException, PortalException;

	public ExportImportConfiguration createExportImportConfiguration(long groupId, long userId, String name, String description, int type, Map<String, Serializable> settingsMap, ServiceContext serviceContext) throws PortalException;

}
