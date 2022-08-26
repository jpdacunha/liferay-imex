package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.profile.ImexProfileService;
import com.liferay.imex.core.api.profile.model.ImexProfile;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ProfileDescriptor;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ProfileDescriptor.CriticityLevel;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ProfilesResource;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.vulcan.pagination.Page;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author jpdacunha
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/profiles.properties",
	scope = ServiceScope.PROTOTYPE, service = ProfilesResource.class
)
public class ProfilesResourceImpl extends BaseProfilesResourceImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(ProfilesResourceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexProfileService imexProfileService;
	
	@Override
	public Page<ProfileDescriptor> getProfilesPage() throws Exception {
		
		List<ProfileDescriptor> descriptors = new ArrayList<>();
		
		List<ImexProfile> imexProfiles = imexProfileService.getSupportedProfiles();
		
		for (ImexProfile imexProfile : imexProfiles) {
			
			ProfileDescriptor descriptor = new ProfileDescriptor();
			
			descriptor.setProfileId(imexProfile.getProfileId());
			descriptor.setName(imexProfile.getName());
			
			String criticity = imexProfile.getCriticity().getValue();
			CriticityLevel criticityLevel = CriticityLevel.create(criticity);
			descriptor.setCriticityLevel(criticityLevel);
			
			descriptors.add(descriptor);
			
		}
		
		return Page.of(descriptors);
	}

	public ImexProfileService getImexProfileService() {
		return imexProfileService;
	}

	public void setImexProfileService(ImexProfileService imexProfileService) {
		this.imexProfileService = imexProfileService;
	}
	
}