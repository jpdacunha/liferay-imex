package com.liferay.imex.core.service.profile.impl;

import com.liferay.imex.core.api.BaseExporterImporter;
import com.liferay.imex.core.api.profile.ImexProfileService;
import com.liferay.imex.core.api.profile.ImexPropertiesProfileReader;
import com.liferay.imex.core.api.profile.model.ImexProfile;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = ImexProfileService.class)
public class ImexProfileServiceImpl implements ImexProfileService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexProfileServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexPropertiesProfileReader profileReader;
	
	/**
	 * Returned supported profiles in array
	 * @return
	 * @throws ImexException 
	 */
	public List<ImexProfile> getSupportedProfiles() throws ImexException {
		
		List<ImexProfile> profiles = null;
		try {
			profiles = profileReader.getSupportedProfiles();
		} catch (SystemException | PortalException e) {
			_log.error(e,e);
			throw new ImexException(e);
		}
		
		return profiles;
		
	}

	/**
	 * Return profile to apply for current Exporter or Importer process
	 * @param profileId
	 * @param bundle
	 * @param exporterOrImporter
	 * @param configAsProperties
	 * @return
	 * @throws ImexException
	 */
	public String getValidProfileId(String profileId, Bundle bundle, BaseExporterImporter exporterOrImporter) throws ImexException {
		
		//Manage profile
		String profile = null;
		
		if (exporterOrImporter.isProfiled()) {
			
			profile =  profileReader.getDefaultProfileId();
			
			if (Validator.isNotNull(profileId)) {
				
				try {
					
					String[] supportedProfiles = profileReader.getSupportedProfilesIds();
					
					if (supportedProfiles != null  && supportedProfiles.length > 0) {
						
						if (!Arrays.asList(supportedProfiles).contains(profileId)) {
							throw new ImexException("Unsupported profile [" + profileId + "].");
						}
						
						profile = profileId;
						
					} else {
						_log.warn("Imex will use default profile because no supported profiles was found.");
					}
					
				} catch (SystemException | PortalException e) {
					_log.error(e,e);
					throw new ImexException(e);
				}
				
			} else {
				_log.debug("[" + bundle.getSymbolicName() + "] is currently using default profile");
			}
			
		} else {
			_log.debug("[" + bundle.getSymbolicName() + "] does not support profile management");
		}
		
		return profile;
		
	}

	@Override
	public String[] getSupportedProfilesIds() throws ImexException {
		try {
			return profileReader.getSupportedProfilesIds();
		} catch (SystemException | PortalException e) {
			_log.error(e,e);
			throw new ImexException(e);
		}
	}

}
