package com.liferay.imex.core.service.report.impl;

import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.util.statics.ReportMessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = ImexExecutionReportService.class)
public class ImexExecutionReportServiceImpl extends BaseExecutionReportServiceImpl implements ImexExecutionReportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexExecutionReportServiceImpl.class);
	
	/* Start Messages*/
	
	public void getStartMessage(Log logger, Company company) {
		getStartMessage(logger, "for COMPANY : " + ReportMessageUtil.getCompanyIdentifier(company), 1);
	}
	
	public void getStartMessage(Log logger, String description) {
		getStartMessage(logger, description, 0);
	}
	
	public void getStartMessage(Log logger, Group group, Locale locale) {		
		getStartMessage(logger, "for GROUP : " + ReportMessageUtil.getGroupIdentifier(group, locale), 2);		
	}
	
	/* Empty Messages */ 
	
	public void getEmpty(Log logger, Group group, Locale locale, String name) {
		getEmpty(logger, name + " " + ReportMessageUtil.getGroupIdentifier(group, locale));
	}
	
	public void getEmpty(Log logger, Group group, Locale locale) {
		getEmpty(logger, ReportMessageUtil.getGroupIdentifier(group, locale));
	}
	
	public void getEmpty(Log logger, String name) {
		getMessage(logger, "[" + name + "] has no elements", 4);
	}
	
	/* Does not exists messages */ 
	
	public void getDNE(Log logger, File name) {
		
		if (name == null ) {
			_log.warn("file is null");
		} else {
			getDNE(logger, name.getAbsolutePath());
		}
	}
	
	public void getDNE(Log logger, String name) {
		getMessage(logger, "[" + name + "] does not exists", 4);
	}
	
	/* Action messages */ 
	
	public void getDisabled(Log logger, String name) {
		getMessage(logger, "[" + name + "] is currently [DISABLED]", 4);
	}
	
	public void getSkipped(Log logger, String name) {
		getMessage(logger, "[" +name + "] [SKIPPED]", 4);
	}
	
	public void getUpdate(Log logger, String name) {
		getMessage(logger, "[" +name + "] [UPDATED]", 4);
	}
	
	public void getCreate(Log logger, String name) {
		getMessage(logger, "[" +name + "] [CREATED]", 4);
	}
	
	public void getOK(Log logger, String name) {
		getMessage(logger, "[" + name + "] [   OK  ]", 4);
	}
	
	public void getOK(Log logger, String key, String name) {
		getMessage(logger, "[" + key + "]=>[" + name + "] [   OK  ]", 4);
	}
	
	public void getOK(Log logger, String key, String name, File file, ImexOperationEnum operation) {
		
		String message = "[" + operation.getValue() + "] [" + key + "]=>[" + name + "] [   OK  ]";		
		if (file != null) {
			message += " - (" + file.getAbsolutePath() + ")";
		} else {
			_log.warn("file is null");
		}
		getMessage(logger, message , 4);
		
	}
	
	/* Error messages */ 
	
	public void getError(Log logger, String name, String error) {
		getMessage(logger, "[" + name + "] [ ERROR ] : " + error, 4);
	}
	
	public void getError(Log logger, Exception e) {
		getError(logger, "[An unexpected error occured]", e.getMessage());
	}
	
	/* Information messages */
	
	public void getMessage(Log logger, Bundle bundle, String description) {
		getMessage(logger, "[" + bundle.getSymbolicName() + "] " + description, 0);
	}
	
	public void getMessage(Log logger, String description) {
		getMessage(logger, description, 0);
	}
	
	public void getMessage(Log logger, String name, String error) {
		getMessage(logger, "[" + name + "] : " + error, 4);
	}
	
	/* End messages */
	
	public void getEndMessage(Log logger, Group group, Locale locale) {		
		getEndMessage(logger, "process for GROUP : " + ReportMessageUtil.getGroupIdentifier(group, locale), 2);		
	}
	
	public void getEndMessage(Log logger, String description) {
		getEndMessage(logger, description, 0);
	}	
	
	/* Collections / Properties */
	public <K> void printKeys (Map<String, K> map, Log logger) {
		
		if (map != null && map.size() > 0) {
			getMessage(logger, "Available keys :");
		}
		
		for (String key : map.keySet()) {
			getMessage(logger, key, 1);
		}
		
	}
	
	/**
	 * Display humane readable properties
	 * @param props
	 * @param bundle
	 */
	public void displayProperties(Properties props, Bundle bundle, Log logger) {
        
		if (props != null) {
				
			for (Entry<Object,Object> value : props.entrySet()) {
				getMessage(logger, ":> " + value.getKey() + " = " + value.getValue(),3);
			}
			
		} 
	                               
	}
	
	public void displayConfigurationLoadingInformation(ImexProperties properties, Log log) {
		displayConfigurationLoadingInformation(properties, log, null);
	}
	
	public void displayConfigurationLoadingInformation(ImexProperties properties, Log log, Bundle bundle) {
		
		if (properties != null) {
			
			if (properties.isDefaulConfiguration()) {
				getMessage(_log, bundle, "is using default configuration loaded from his embedded [" + properties.getPath() + "].");
			} else if (Validator.isNotNull(properties.getPath())) {
				getMessage(_log, bundle, "is using configuration loaded from [" + properties.getPath() + "].");
			} else {
				getError(_log, "Configuration error", "Configuration is not properly loaded");
			}
			
		} else {
			getMessage(log, "Properties are null : no properties to print");
		}
		
	}

}
