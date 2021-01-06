package com.liferay.imex.site.service.impl;

import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component
public class SiteCommonServiceImpl implements SiteCommonService {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Override
	public String getParentSiteFriendlyURL(long companyId, long parentGroupId) throws PortalException {
		
		if (parentGroupId != GroupConstants.DEFAULT_PARENT_GROUP_ID) {
			
			Group group = groupLocalService.getGroup(parentGroupId);
			
			//group cannot be null here because liferay is throwing NoSuchGroupException if no group is found
			return group.getFriendlyURL();
			
		}
		
		return null;
		
	}
	
	@Override
	public Group getSiteByFriendlyURL(long companyId, String friendlyURL) throws PortalException {
		
		if (Validator.isNull(friendlyURL)) {
			return null;
		}
		
		return groupLocalService.getFriendlyURLGroup(companyId, friendlyURL);
	}
	
	@Override
	public long getSiteParentGroupId(long companyId, String parentGroupIdFriendlyUrl) throws PortalException {
		
		long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
		Group parentGroup = this.getSiteByFriendlyURL(companyId, parentGroupIdFriendlyUrl);
		if (parentGroup != null) {
			parentGroupId = parentGroup.getGroupId();
		}
		
		return parentGroupId;
		
	}

	public GroupLocalService getGroupLocalService() {
		return groupLocalService;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

}
