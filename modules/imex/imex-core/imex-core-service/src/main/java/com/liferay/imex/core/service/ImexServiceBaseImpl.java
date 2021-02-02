package com.liferay.imex.core.service;

import com.liferay.imex.core.api.BaseExporterImporter;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.Properties;

import org.osgi.framework.Bundle;

/**
 * Define inherited behaviors from Exporter and Importer core service
 * @author dev
 *
 */
public abstract class ImexServiceBaseImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexServiceBaseImpl.class);

	/**
	 * Return profile to apply for current Exporter or Importer process
	 * @param profileId
	 * @param bundle
	 * @param exporterOrImporter
	 * @param configAsProperties
	 * @return
	 * @throws ImexException
	 */
	public String getValidProfile(String profileId, Bundle bundle, BaseExporterImporter exporterOrImporter, Properties configAsProperties) throws ImexException {
		
		//Manage profile
		String profile = null;
		
		if (exporterOrImporter.isProfiled()) {
			
			profile =  GetterUtil.getString(configAsProperties.get(ImExCorePropsKeys.DEFAULT_PROFILE_NAME));
			
			if (Validator.isNull(profile)) {
				throw new ImexException("Missing mandatory parameter value [" + ImExCorePropsKeys.DEFAULT_PROFILE_NAME + "]. Please check your configuration file");
			}
			
			if (Validator.isNotNull(profileId)) {
				
				String[] supportedProfiles = CollectionUtil.getArray(configAsProperties.getProperty(ImExCorePropsKeys.MANAGES_PROFILES_LIST));
				
				if (supportedProfiles != null  && supportedProfiles.length > 0) {
					
					if (!Arrays.asList(supportedProfiles).contains(profileId)) {
						throw new ImexException("Unsupported profile [" + profileId + "]. Please check [" + ImExCorePropsKeys.MANAGES_PROFILES_LIST + "] parameter to manage supported profiles list.");
					}
					
					profile = profileId;
					
					
				} else {
					_log.warn("Imex will user default profile because no supported profiles was found. Please check [" + ImExCorePropsKeys.MANAGES_PROFILES_LIST + "] if you want to define a profile list.");
				}
				
			} else {
				_log.debug("[" + bundle.getSymbolicName() + "] is currently using default profile");
			}
			
		} else {
			_log.debug("[" + bundle.getSymbolicName() + "] does not support profile management");
		}
		
		return profile;
		
	}

}
