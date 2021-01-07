package com.liferay.imex.core.service.lar.impl;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.service.ExportImportLocalService;
import com.liferay.imex.core.api.lar.ImexLarService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
	
	@Override
	public void doExport(ExportImportConfiguration exportImportConfiguration, File destinationDir, String fileName) throws SystemException, PortalException {
		
		com.liferay.imex.core.util.statics.FileUtil.isValidDirectory(destinationDir);	
		
		if (Validator.isNull(fileName)) {
			throw new SystemException("Invalid file name");
		}
		
		File larFile = exportImportService.exportLayoutsAsFile(exportImportConfiguration);
		
		FileUtil.move(larFile, new File(destinationDir.getAbsolutePath() + StringPool.SLASH +  fileName));
		
		
	}
	
	@Override
	public long doImport(User user, ExportImportConfiguration exportImportConfiguration, File sourceDir, String fileName) throws SystemException, PortalException {
			
		if (Validator.isNull(user)) {
			throw new SystemException("Invalid user");
		}
		
		com.liferay.imex.core.util.statics.FileUtil.isValidDirectory(sourceDir);	
		
		if (Validator.isNull(fileName)) {
			throw new SystemException("Invalid file name");
		}
		
		long userId = user.getUserId();
		
		File toImport = new File(sourceDir.getAbsolutePath() + StringPool.SLASH +  fileName);
		
		exportImportService.importLayouts(exportImportConfiguration, toImport);
		
		//TODO : JDA manage export in background
		//return exportImportService.importLayoutsInBackground(userId, exportImportConfiguration, toImport);
		
		return 0;
	}
	
	@Override
	public ExportImportConfiguration createExportImportConfiguration(long groupId, long userId, String name, String description, int type, Map<String, Serializable> settingsMap, ServiceContext serviceContext) throws PortalException {
	
		return exportImportConfigurationLocalService.addExportImportConfiguration(userId, groupId, name, description, type, settingsMap, serviceContext);
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String[]> buildParameterMapFromProperties(Properties props, String prefix) {
		
		if (props == null) {
			return null;
		}
		
		boolean removePrefix = true;
		Properties parameterProperties = PropertiesUtil.getProperties(props, prefix, removePrefix);
		
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		for (Map.Entry entry : parameterProperties.entrySet()) {
			String key = (String)entry.getKey();
			String[] value = ((String)entry.getValue()).split(",");
			parameterMap.put(key, value);
		}
		
		return parameterMap;
	}
	
}
