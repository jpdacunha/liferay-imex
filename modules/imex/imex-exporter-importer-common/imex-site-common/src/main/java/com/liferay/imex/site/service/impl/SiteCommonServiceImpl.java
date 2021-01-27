package com.liferay.imex.site.service.impl;

import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.site.service.SiteCommonService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
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
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	@Transactional
	public void eraseSiteHierarchy(Group group) throws PortalException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Group.class, classLoader)
	            .add(RestrictionsFactoryUtil.eq("parentGroupId", group.getGroupId()))
	            .setProjection(ProjectionFactoryUtil.property("groupId"));
		
		List<Long> siteChilds = groupLocalService.dynamicQuery(dynamicQuery);
		
		for (long childGroupId : siteChilds) {
			
			Group childGroup = groupLocalService.getGroup(childGroupId);
			this.detachGroupFromParent(childGroup);
			reportService.getMessage(_log, "Detached [" + childGroup.getFriendlyURL() + "] from [" + group.getFriendlyURL() + "] ...");
			
		}
				
	}
	
	@Override
	public Group detachGroupFromParent(Group group) throws PortalException {
		
		long defaultParentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
		
		return groupLocalService.updateGroup(group.getGroupId(), defaultParentGroupId, group.getNameMap(), group.getDescriptionMap(), group.getType(), group.isManualMembership(), group.getMembershipRestriction(), group.getFriendlyURL(), group.isInheritContent(), group.isActive(), new ServiceContext());
		
	}
	
	@Override
	public Group attachToParentSite(Group group, String parentGroupFriendlyURL) throws PortalException {
		
		long companyId = group.getCompanyId();
		long parentGroupId = getSiteParentGroupId(companyId, parentGroupFriendlyURL);
		Group returnedGroup = attachToParentSite(group, parentGroupId);
		if (parentGroupId != GroupConstants.DEFAULT_PARENT_GROUP_ID) {
			reportService.getMessage(_log, "Attached [" + group.getFriendlyURL() + "] to [" + parentGroupFriendlyURL + "] ...");
		} else {
			_log.debug("[" + group.getFriendlyURL() + "] was attached to default group");
		}
		return returnedGroup;
	}
	
	private Group attachToParentSite(Group group, long parentGroupId) throws PortalException {
			
		Group returnedGroup = groupLocalService.updateGroup(group.getGroupId(), parentGroupId, group.getNameMap(), group.getDescriptionMap(), group.getType(), group.isManualMembership(), group.getMembershipRestriction(), group.getFriendlyURL(), group.isInheritContent(), group.isActive(), new ServiceContext());
		return returnedGroup;
		
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

	public ImexExecutionReportService getReportService() {
		return reportService;
	}

	public void setReportService(ImexExecutionReportService reportService) {
		this.reportService = reportService;
	}

}
