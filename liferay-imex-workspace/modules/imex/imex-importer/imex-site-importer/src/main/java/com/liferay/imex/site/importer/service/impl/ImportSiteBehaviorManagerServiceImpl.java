package com.liferay.imex.site.importer.service.impl;

import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.site.importer.configuration.ImExSiteImporterPropsKeys;
import com.liferay.imex.site.importer.service.ImportSiteBehaviorManagerService;
import com.liferay.imex.site.model.OnExistsSiteMethodEnum;
import com.liferay.imex.site.model.OnMissingSiteMethodEnum;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component
public class ImportSiteBehaviorManagerServiceImpl implements ImportSiteBehaviorManagerService {
	
	private static Log _log = LogFactoryUtil.getLog(ImportSiteBehaviorManagerServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	public OnMissingSiteMethodEnum getOnMissingBehavior(Properties config, String friendlyURL) {
		
		if (friendlyURL != null) {
			
			String suffix = "." + friendlyURL.replaceAll("/", "");
			String value = getOnMissingBehaviorForSite(suffix, config);
			if (value != null && !value.equals("")) {
				
				reportService.getMessage(_log, "Loading specific behavior : [" + value + "] for group [" + friendlyURL + "] ...");
				OnMissingSiteMethodEnum converted = OnMissingSiteMethodEnum.fromValue(value);				
				if (converted != null) {
					return converted;
				}
				
			}
			
		}
		
		String defaultValue = GetterUtil.getString(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_ON_MISSING));
		OnMissingSiteMethodEnum defaultEnumValue = OnMissingSiteMethodEnum.fromValue(defaultValue);
		
		if (Validator.isNull(defaultEnumValue)) {
			_log.error("Parameter identified by [" + ImExSiteImporterPropsKeys.IMPORT_SITE_ON_MISSING + "] is not properly configured. Please check your configuration file.");
			defaultEnumValue = OnMissingSiteMethodEnum.SKIP;
		}
		
		return defaultEnumValue;
		
	}
	
	@Override
	public OnExistsSiteMethodEnum getOnExistsBehavior(Properties config, Group group) {
		
		if (group != null) {
			
			String suffix = "." + group.getFriendlyURL().replaceAll("/", "");
			String value = getOnExistsBehaviorForSite(suffix, config);
			if (value != null && !value.equals("")) {
				
				reportService.getMessage(_log, "Loading specific behavior : [" + value + "] for group [" + group.getFriendlyURL() + "] ...");
				OnExistsSiteMethodEnum converted = OnExistsSiteMethodEnum.fromValue(value);				
				if (converted != null) {
					return converted;
				}
				
			}
			
		}
		
		String defaultValue = GetterUtil.getString(config.get(ImExSiteImporterPropsKeys.IMPORT_SITE_ON_EXISTS));
		OnExistsSiteMethodEnum defaultEnumValue = OnExistsSiteMethodEnum.fromValue(defaultValue);	
		
		if (Validator.isNull(defaultEnumValue)) {
			_log.error("Parameter identified by [" + ImExSiteImporterPropsKeys.IMPORT_SITE_ON_EXISTS + "] is not properly configured. Please check your configuration file.");
			defaultEnumValue = OnExistsSiteMethodEnum.SKIP;
		}
		
		return defaultEnumValue;
		
	}
	
	private String getOnExistsBehaviorForSite(String suffix, Properties config) {
		
		return getBehaviorForSite(ImExSiteImporterPropsKeys.IMPORT_SITE_ON_EXISTS, suffix, config);
		
	}
	
	private String getOnMissingBehaviorForSite(String suffix, Properties config) {
		
		return getBehaviorForSite(ImExSiteImporterPropsKeys.IMPORT_SITE_ON_MISSING, suffix, config);
		
	}
	
	private String getBehaviorForSite(String prefix, String suffix, Properties config) {
		
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
