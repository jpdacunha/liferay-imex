package com.liferay.imex.site.service.impl;

import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.persistence.GroupFinder;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service = SiteCommonService.class)
public class SiteCommonServiceImpl implements SiteCommonService {
	
	private static final Log _log = LogFactoryUtil.getLog(SiteCommonServiceImpl.class);
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupLocalService groupLocalService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected GroupFinder groupFinder;
	
	private static final int MAX_CHILDS = 500;
	
	@Override
	@Transactional
	public void eraseSiteHierarchy(Group group) throws PortalException {
		
		List<Long> siteChilds = groupFinder.findByC_P(group.getCompanyId(), group.getGroupId(), -1, MAX_CHILDS);
		
		for (long childGroupId : siteChilds) {
			Group childGroup = groupLocalService.getGroup(childGroupId);
			_log.info("Detaching [" + childGroup.getFriendlyURL() + "] from [" + group.getFriendlyURL() + "] ...");
			this.detachGroupFromParent(childGroup);
		}
				
	}
	
	@Override
	public Group detachGroupFromParent(Group group) throws PortalException {
		
		long defaultParentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
		
		return groupLocalService.updateGroup(group.getGroupId(), defaultParentGroupId, group.getNameMap(), group.getDescriptionMap(), group.getType(), group.isManualMembership(), group.getMembershipRestriction(), group.getFriendlyURL(), group.isInheritContent(), group.isActive(), new ServiceContext());
		
	}
	
	@Override
	public Group attachToParentSite(Group group, long parentGroupId) throws PortalException {
			
		return groupLocalService.updateGroup(group.getGroupId(), parentGroupId, group.getNameMap(), group.getDescriptionMap(), group.getType(), group.isManualMembership(), group.getMembershipRestriction(), group.getFriendlyURL(), group.isInheritContent(), group.isActive(), new ServiceContext());
		
	}
	
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

	public GroupFinder getGroupFinder() {
		return groupFinder;
	}

	public void setGroupFinder(GroupFinder groupFinder) {
		this.groupFinder = groupFinder;
	}

}
