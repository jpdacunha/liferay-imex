package com.liferay.imex.core.service.report.impl;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.util.statics.ReportMessageUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.labelers.TimestampLabeler;
import org.pmw.tinylog.policies.DailyPolicy;
import org.pmw.tinylog.writers.RollingFileWriter;

@Component(immediate = true, service = ImexExecutionReportService.class)
public class ImexExecutionReportServiceImpl implements ImexExecutionReportService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexExecutionReportServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	private Configurator configurator;
	
	private Boolean displayInLiferayLogs = null;

	protected final static String PREFIX = "[IMEX] : ";
	
	/* Root methods */

	public void getSeparator(Log logger) {
		imexLog(logger, StringPool.BLANK);
	}
	
	public void getStartMessage(Log logger, String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "[:STARTING:]";
		
		if (Validator.isNotNull(description)){
			rootMessage += StringPool.SPACE + description;
		}
		
		rootMessage = ReportMessageUtil.pad(rootMessage, nbPadLeft);
		
		imexLog(logger, rootMessage);
		
	}


	public void getMessage(Log logger, String description, int nbPadLeft) {
		
		String rootMessage = StringPool.BLANK;
		
		if (Validator.isNotNull(description)){
			rootMessage = PREFIX + description;
		}
		
		rootMessage = ReportMessageUtil.pad(rootMessage, nbPadLeft);
		
		imexLog(logger, rootMessage);
		
	}
	
	
	public void getEndMessage(Log logger, String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "[:END OF:]";
		
		if (Validator.isNotNull(description)){
			rootMessage += StringPool.SPACE + description;
		}
		
		rootMessage = ReportMessageUtil.pad(rootMessage, nbPadLeft);
		
		imexLog(logger, rootMessage);
		
	}

	
	public void getPropertyMessage(Log logger, String key, String value) {
		getPropertyMessage(logger, key, value, 0);
	}
	
	
	public void getPropertyMessage(Log logger, String key, String value, int nbPadLeft) {
		
		String rootMessage = PREFIX;
		
		if (Validator.isNotNull(key) && Validator.isNotNull(value)) {
			
			rootMessage += key + " : " + value;
			
		} else {
			rootMessage = StringPool.BLANK;
		}
		
		rootMessage = ReportMessageUtil.pad(rootMessage, nbPadLeft);
		
		imexLog(logger, rootMessage);
		
	}
	
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
	
	@Override
	public void getOK(Log logger, String key, String name, String description) {
		getMessage(logger, "[" + key + "]=>[" + name + "] " + description + " [   OK  ]", 4);
		
	}
	
	public void getOK(Log logger, String key, String name, ImexOperationEnum operation) {
		getOK(logger, key, name, null, operation);	
	}
	
	public void getOK(Log logger, String key, String name, File file, ImexOperationEnum operation) {
		
		String message = "[" + operation.getValue() + "] [" + key + "]=>[" + name + "] [   OK  ]";		
		if (file != null) {
			message += " - (" + file.getAbsolutePath() + ")";
		} else {
			_log.debug("file is null");
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
	
	public void getError(Log logger, String name, Exception e) {
		String stacktrace = ExceptionUtils.getStackTrace(e);
		getError(logger, name, e.getMessage() + " " + stacktrace);
	}
	
	/* Information messages */
	
	public void getMessage(Log logger, Bundle bundle, String description) {
		if (bundle != null) {
			getMessage(logger, "[" + bundle.getSymbolicName() + "] " + description, 0);
		} else {
			getMessage(logger, description, 0);
		}
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
		Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		displayConfigurationLoadingInformation(properties, log, bundle);
	}
	
	public void displayConfigurationLoadingInformation(ImexProperties properties, Log log, Bundle bundle) {
		
		if (properties != null) {

			String paths = StringPool.BLANK;
			int i = 0;
			for (String path : properties.getPath()) {
				if (i == 0) {
					paths += path;
				} else {
					paths += " , " + path;
				}
				i++;
			}
			
			getMessage(_log, bundle, "is using configuration loaded from [" + paths + "].");
			
		} else {
			getMessage(log, "Properties are null : no properties to print");
		}
		
	}
	
	public Configurator getConfigurator() {
		return configurator;
	}

	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}
	
	private void initializeLogger(String logsPath) {
		
		if (configurator == null) {
			
			_log.debug("Initializing logger ...");
			
			configurator = Configurator.defaultConfig();
			configurator.level(Level.DEBUG);
			
			configurator.formatPattern("{date:yyyy-MM-dd HH:mm:ss} {level} [{thread}][{context:" + ImexExecutionReportService.IDENTIFIER_KEY + "}] : {message}");
			
			if (_log.isDebugEnabled()) {
				_log.debug("IMEX output log path : " + logsPath);
			}
			
			RollingFileWriter writer = new RollingFileWriter(logsPath + "/imex.log", 3, new TimestampLabeler("yyyy-MM-dd"), new DailyPolicy());
			
			configurator.addWriter(writer);
			
			configurator.activate();
				
			_log.debug("Done.");
			
		}
		
		if (this.displayInLiferayLogs == null) {
			
			ImexProperties coreConfig = new ImexProperties();
			configurationService.loadCoreConfiguration(coreConfig);
			this.displayInLiferayLogs = GetterUtil.getBoolean(coreConfig.getProperties().get(ImExCorePropsKeys.DISPLAY_EXECUTION_IN_LIFERAY_LOGS));
			
		}
		
	}
	
	private void imexLog(Log logger, String toLog) {
		
		String logsPath = configurationService.getImexLogsPath();
		
		initializeLogger(logsPath);
		
		if (displayInLiferayLogs) {
			if (logger != null) {
				
				logger.info(toLog);
				
			} else {
				
				_log.error("Mandatory object logger is null unable to log [" + toLog + "]");
				
			}	
		}
		
		Logger.info(toLog);
			
	}

}
