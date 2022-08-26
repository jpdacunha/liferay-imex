package com.liferay.imex.core.service.profile.impl;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.api.profile.ImexPropertiesProfileReader;
import com.liferay.imex.core.api.profile.model.ImexProfile;
import com.liferay.imex.core.api.profile.model.ImexProfileCriticityEnum;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component
public class ImexPropertiesProfileReaderImpl implements ImexPropertiesProfileReader {
	
	private static Log _log = LogFactoryUtil.getLog(ImexPropertiesProfileReaderImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;


	@Override
	public ImexProfile geProfile(String profileId) throws PortalException, SystemException {
		
		if (Validator.isNull(profileId)) {
			throw new SystemException("Missing mandatory parameter");
		}
		
		ImexProfile profile = null;
		ImexProperties config = new ImexProperties();
		configurationService.loadCoreConfiguration(config);
		Properties configAsProperties = config.getProperties();
		
		String[] supportedProfiles = getSupportedProfilesIds();
		
		//Search passed profile in supported profiles array
		Optional<String> optional = Arrays.stream(supportedProfiles).filter(p -> p.equals(profileId)).findFirst();
		
		if (optional.isPresent()) {
			
			String currentProfileId = optional.get();
			
			if (currentProfileId.equals(profileId)) {
				
				profile = new ImexProfile(profileId);
				
				//Name
				String profileNameKey = buildNameKey(currentProfileId);
				String profileName =  GetterUtil.getString(configAsProperties.get(profileNameKey));
				if (Validator.isNull(profileName)) {
					_log.warn("Unable to read profile name. Please check that you have properly configured [" + profileNameKey + "] parameter in configuration file");
					profile.setName(profileId);
				} else {
					profile.setName(profileName);
				}
				
				//Description
				String profileDescriptionKey = buildDescriptionKey(currentProfileId);
				String profileDescription =  GetterUtil.getString(configAsProperties.get(profileDescriptionKey));
				if (Validator.isNull(profileDescription)) {
					_log.warn("Unable to read profile description. Please check that you have properly configured [" + profileDescriptionKey + "] parameter in configuration file");
				} else {
					profile.setDescription(profileDescription);
				}
				
				//Criticity
				String profileCriticityKey = buildCriticityKey(currentProfileId);
				String profileCriticity =  GetterUtil.getString(configAsProperties.get(profileCriticityKey));
				if (Validator.isNull(profileCriticity)) {
					_log.warn("Unable to read profile Criticity. Please check that you have properly configured [" + profileCriticityKey + "] parameter in configuration file");
					profile.setCriticity(ImexProfileCriticityEnum.NORMAL);
				} else {
					ImexProfileCriticityEnum imexProfileCriticityEnum = ImexProfileCriticityEnum.toImexProfileCriticityEnum(profileCriticity, ImexProfileCriticityEnum.NORMAL);
					profile.setCriticity(imexProfileCriticityEnum);
				}
				
				return profile;
				
			}
			
		} else {
			throw new SystemException("Unknown profile [" + profileId + "]");
		} 
		return profile;
	}

	@Override
	public List<ImexProfile> getSupportedProfiles() throws PortalException, SystemException {
		
		List<ImexProfile> profiles = new ArrayList<ImexProfile>();
		
		String[] supportedProfiles = getSupportedProfilesIds();
		
		for (String currentProfileId : supportedProfiles) {
			
			ImexProfile imexProfile = geProfile(currentProfileId);
			profiles.add(imexProfile);
		}

		return profiles;
		
	}

	@Override
	public String[] getSupportedProfilesIds() throws PortalException, SystemException {
		
		ImexProperties config = new ImexProperties();
		configurationService.loadCoreConfiguration(config);
		Properties configAsProperties = config.getProperties();
		
		String[] supportedProfiles = CollectionUtil.getArray(configAsProperties.getProperty(ImExCorePropsKeys.MANAGES_PROFILES_LIST));
		
		return supportedProfiles;
		
	}

	@Override
	public String getDefaultProfileId() {

		ImexProperties config = new ImexProperties();
		configurationService.loadCoreConfiguration(config);
		Properties configAsProperties = config.getProperties();
		String profile =  GetterUtil.getString(configAsProperties.get(ImExCorePropsKeys.DEFAULT_PROFILE_NAME));
		
		if (Validator.isNull(profile)) {
			throw new SystemException("Missing mandatory parameter value [" + ImExCorePropsKeys.DEFAULT_PROFILE_NAME + "]. Please check your configuration file");
		}
		
		return profile;
		
	}
	
	private static String buildNameKey(String profileId) {
		
		return buildKey(profileId, ImExCorePropsKeys.PROFILE_NAME_SUFIX_KEY);
		
	}
	
	private static String buildDescriptionKey(String profileId) {
		
		return buildKey(profileId, ImExCorePropsKeys.PROFILE_DESCRIPTION_SUFIX_KEY);
		
	}
	
	private static String buildCriticityKey(String profileId) {
		
		return buildKey(profileId, ImExCorePropsKeys.PROFILE_CRITICITY_SUFIX_KEY);
		
	}
	
	private static String buildKey(String profileId, String suffixedValue) {
		
		StringBuilder key = null;
			
		if (profileId != null && !profileId.equals("")) {
			
			key = new StringBuilder();
			key.append(ImExCorePropsKeys.PROFILE_PREFIX_KEY);
			key.append(StringPool.PERIOD);
			key.append(profileId);
			key.append(StringPool.PERIOD);
			key.append(suffixedValue);
			
		} else {
			_log.error("Unsupported profileId:[" + profileId + "]");
		}
			
		if (key == null) {
			return null;
		}
		return key.toString();
		
	}
	
	

	public ImexConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ImexConfigurationService configurationService) {
		this.configurationService = configurationService;
	}


}
