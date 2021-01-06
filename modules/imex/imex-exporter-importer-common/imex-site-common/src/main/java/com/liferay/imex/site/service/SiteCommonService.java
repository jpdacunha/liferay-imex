package com.liferay.imex.site.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;

public interface SiteCommonService {

	public String getParentSiteFriendlyURL(long companyId, long parentGroupId) throws PortalException;

	public Group getSiteByFriendlyURL(long companyId, String friendlyURL) throws PortalException;

	public long getSiteParentGroupId(long companyId, String parentGroupIdFriendlyUrl) throws PortalException;

}
