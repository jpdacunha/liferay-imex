package com.liferay.imex.core.service.importer.impl;

import com.liferay.imex.core.api.importer.ImportBehaviorManagerService;
import com.liferay.imex.core.api.model.OnExistsMethodEnum;
import com.liferay.imex.core.api.model.OnMissingMethodEnum;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = ImportBehaviorManagerService.class)
public class ImportBehaviorManagerServiceImpl implements ImportBehaviorManagerService {
	
	private static Log _log = LogFactoryUtil.getLog(ImportBehaviorManagerServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	public OnMissingMethodEnum getOnMissingBehavior(Properties config, String friendlyURL, String prefix) throws ImexException {
		
		if (Validator.isNull(prefix)) {
			throw new ImexException("Invalid configuration prefix");
		}
		
		if (friendlyURL != null) {
			
			String suffix = "." + friendlyURL.replaceAll("/", "");
			String value = getOnMissingBehaviorValue(prefix, suffix, config);
			if (value != null && !value.equals("")) {
				
				reportService.getMessage(_log, "Loading specific behavior : [" + value + "] for group [" + friendlyURL + "] ...");
				OnMissingMethodEnum converted = OnMissingMethodEnum.fromValue(value);				
				if (converted != null) {
					return converted;
				}
				
			}
			
		}
		
		String defaultValue = GetterUtil.getString(config.get(prefix));
		OnMissingMethodEnum defaultEnumValue = OnMissingMethodEnum.fromValue(defaultValue);
		
		if (Validator.isNull(defaultEnumValue)) {
			_log.error("Parameter identified by [" + prefix + "] is not properly configured. Please check your configuration file.");
			defaultEnumValue = OnMissingMethodEnum.SKIP;
		}
		
		return defaultEnumValue;
		
	}
	
	@Override
	public OnExistsMethodEnum getOnExistsBehavior(Properties config, String friendlyURL, String prefix) throws ImexException {
		
		if (Validator.isNull(prefix)) {
			throw new ImexException("Invalid configuration prefix");
		}
		
		if (friendlyURL != null) {
			
			String suffix = "." + friendlyURL.replaceAll("/", "");
			String value = getOnExistsBehaviorValue(prefix, suffix, config);
			if (value != null && !value.equals("")) {
				
				reportService.getMessage(_log, "Loading specific behavior : [" + value + "] for group [" + friendlyURL + "] ...");
				OnExistsMethodEnum converted = OnExistsMethodEnum.fromValue(value);				
				if (converted != null) {
					return converted;
				}
				
			}
			
		}
		
		String defaultValue = GetterUtil.getString(config.get(prefix));
		OnExistsMethodEnum defaultEnumValue = OnExistsMethodEnum.fromValue(defaultValue);	
		
		if (Validator.isNull(defaultEnumValue)) {
			_log.error("Parameter identified by [" + prefix + "] is not properly configured. Please check your configuration file.");
			defaultEnumValue = OnExistsMethodEnum.SKIP;
		}
		
		return defaultEnumValue;
		
	}
	
	private String getOnExistsBehaviorValue(String prefix, String suffix, Properties config) {
		
		return getBehavior(prefix, suffix, config);
		
	}
	
	private String getOnMissingBehaviorValue(String prefix, String suffix, Properties config) {
		
		return getBehavior(prefix, suffix, config);
		
	}
	
	private String getBehavior(String prefix, String suffix, Properties config) {
		
		String value = GetterUtil.getString(config.get(prefix + suffix));
		
		//Trying with uppercase name
		if (value == null || value.equals("")) {
			suffix = suffix.toUpperCase(Locale.ENGLISH);
			value = GetterUtil.getString(config.get(prefix + suffix));
		}
		
		return value;
		
	}

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}

}
