package com.liferay.imex.core.api.report;

import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Bundle;

public interface ImexExecutionReportService {
	
	public void getPropertyMessage(Log logger, String key, String value);
	
	public void getPropertyMessage(Log logger, String key, String value, int nbPadLeft);
	
	public void getSeparator(Log logger);
	
	/* Start messages */
	
	public void getStartMessage(Log logger, Company company);
	
	public void getStartMessage(Log logger, String description);
	
	public void getStartMessage(Log logger, Group group, Locale locale);
	
	public void getStartMessage(Log log, String description, int i);
	
	/* Empty Messages */ 
	
	public void getEmpty(Log logger, Group group, Locale locale, String name);
	
	public void getEmpty(Log logger, Group group, Locale locale);
	
	public void getEmpty(Log logger, String name);
	
	/* Does not exists messages */ 
	
	public void getDNE(Log logger, File name);
	
	public void getDNE(Log logger, String name);
	
	/* Action messages */ 
	
	public void getDisabled(Log logger, String name);
	
	public void getSkipped(Log logger, String name);
	
	public void getUpdate(Log logger, String name);
	
	public void getCreate(Log logger, String name);
	
	public void getOK(Log logger, String name);
	
	public void getOK(Log logger, String key, String name);
	
	public void getOK(Log logger, String key, String name, File file, ImexOperationEnum operation);
	
	/* Error messages */ 
	
	public void getError(Log logger, String name, String error);
	
	public void getError(Log logger, Exception e);
	
	/* Information messages */
	
	public void getMessage(Log logger, Bundle bundle, String description);
	
	public void getMessage(Log logger, String description);
	
	public void getMessage(Log logger, String name, String error);
	
	public void getMessage(Log logger, String description, int nbPadLeft);
	
	/* End messages */
	
	public void getEndMessage(Log logger, String description, int nbPadLeft);
	
	public void getEndMessage(Log logger, Group group, Locale locale);
	
	public void getEndMessage(Log logger, String description);
	
	/* Collections */
	
	public <K> void printKeys (Map<String, K> map, Log logger);
	
	public void displayProperties(Properties props, Bundle bundle, Log logger);
	
	public void displayConfigurationLoadingInformation(ImexProperties properties, Log log);
	
	public void displayConfigurationLoadingInformation(ImexProperties properties, Log log, Bundle bundle);
	
}
