package com.liferay.imex.core.service.lar.impl;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.service.ExportImportLocalService;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = ImexLarService.class)
public class ImexLarServiceImpl implements ImexLarService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexLarServiceImpl.class);

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ExportImportLocalService exportImportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ExportImportConfigurationLocalService exportImportConfigurationLocalService;
	
	public void doImport(long userId, Group group, File groupDir, File larFile) throws SystemException, PortalException {
		
		//ExportImportConfiguration exportImportConfiguration = 
		
		//_exportImportService.exportLayoutsAsFile(exportImportConfiguration)
		
	}
	
	@Override
	public ExportImportConfiguration createExportImportConfiguration(long groupId, long userId, String name, String description, int type, Map<String, Serializable> settingsMap, ServiceContext serviceContext) throws PortalException {
	
		return exportImportConfigurationLocalService.addExportImportConfiguration(userId, groupId, name, description, type, settingsMap, serviceContext);
		
	}
	
}
