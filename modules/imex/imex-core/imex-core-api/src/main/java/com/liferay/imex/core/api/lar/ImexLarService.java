package com.liferay.imex.core.api.lar;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

public interface ImexLarService {
	
	public ExportImportConfiguration createExportImportConfiguration(long groupId, long userId, String name, String description, int type, Map<String, Serializable> settingsMap, ServiceContext serviceContext) throws PortalException;

	public Map<String, String[]> buildParameterMapFromProperties(Properties props, String prefix);

	public void doExport(ExportImportConfiguration exportImportConfiguration, File destinationDir, String fileName) throws SystemException, PortalException;
		
}
