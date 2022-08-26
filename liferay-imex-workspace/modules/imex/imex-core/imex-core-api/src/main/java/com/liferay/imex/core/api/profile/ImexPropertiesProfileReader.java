package com.liferay.imex.core.api.profile;

import com.liferay.imex.core.api.profile.model.ImexProfile;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import java.util.List;

public interface ImexPropertiesProfileReader {
	
	/**
	 * 
	 * @param profileId
	 * @param props
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public ImexProfile geProfile(String profileId) throws PortalException, SystemException;
	
	/**
	 * 
	 * @param props
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public List<ImexProfile> getSupportedProfiles() throws PortalException, SystemException;
	
	/**
	 * 
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public String[] getSupportedProfilesIds() throws PortalException, SystemException;
	
	/**
	 * 
	 * @return
	 */
	public String getDefaultProfileId();
	

}
