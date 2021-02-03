package com.liferay.imex.virtualhost.service;

import com.liferay.portal.kernel.model.VirtualHost;

import java.util.List;

public interface VirtualhostCommonService {
	
	public static final int DEFAULT_LAYOUTSET_ID = 0;

	public List<VirtualHost> getCompanyVirtualHost(long companyId);

	public List<VirtualHost> getLayoutSetIdVirtualHosts(long companyId, long layoutSetId);

}
