package com.liferay.imex.core.api.profile;

import com.liferay.imex.core.api.BaseExporterImporter;
import com.liferay.imex.core.api.profile.model.ImexProfile;
import com.liferay.imex.core.util.exception.ImexException;

import java.util.List;

import org.osgi.framework.Bundle;

public interface ImexProfileService {
	
	public List<ImexProfile> getSupportedProfiles() throws ImexException;;
	
	public String getValidProfileId(String profileId, Bundle bundle, BaseExporterImporter exporterOrImporter) throws ImexException;

	public String[] getSupportedProfilesIds() throws ImexException;

}
