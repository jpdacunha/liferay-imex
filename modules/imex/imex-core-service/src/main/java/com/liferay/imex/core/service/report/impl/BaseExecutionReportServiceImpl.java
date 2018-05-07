package com.liferay.imex.core.service.report.impl;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.util.statics.ReportMessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.RollingFileWriter;

public class BaseExecutionReportServiceImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(BaseExecutionReportServiceImpl.class);
	
	private Configurator configurator;
	
	protected final static String PREFIX = "[IMEX] : ";
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
		
	public Configurator getConfigurator() {
		return configurator;
	}

	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}

	public BaseExecutionReportServiceImpl() {
		super();
	}
	
	public void getSeparator(Log logger) {
		imexLog(logger, StringPool.BLANK);
	}
	
	public void getStartMessage(Log logger, String description, int nbPadLeft) {
		
		String rootMessage = PREFIX + "Starting";
		
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
		
		String rootMessage = PREFIX + "End of";
		
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
	
	private synchronized void initializeLogger() {
		
		if (configurator == null) {
			
			_log.info("Initializing logger ...");
			
			configurator = Configurator.defaultConfig();
			configurator.level(Level.DEBUG);
			
			String logsPath = configurationService.getImexLogsPath();
			
			RollingFileWriter writer = new RollingFileWriter(logsPath + "/imex.log", 3);
			
			//TODO : JDA implement my own writer (http://www.tinylog.org/extend)
			
			//writer.init(configuration);
			
			//configurator.addWriter(new FileWriter(logsPath + "/imex.log"));
			
			configurator.addWriter(writer);
			
			configurator.activate();
			
			_log.info("Done.");
			
		}
		
	}
	
	private void imexLog(Log logger, String toLog) {
		
		initializeLogger();
		
		if (logger != null) {
			
			logger.info(toLog);
			Logger.info(toLog);
			
		} else {
			
			_log.error("Mandatory object logger is null");
			
		}
		
	}
	


}
