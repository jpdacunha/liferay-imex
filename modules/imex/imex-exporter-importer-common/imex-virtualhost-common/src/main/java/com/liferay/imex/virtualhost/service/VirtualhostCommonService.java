package com.liferay.imex.virtualhost.service;

import com.liferay.portal.kernel.model.VirtualHost;

import java.util.List;

public interface VirtualhostCommonService {

	public List<VirtualHost> getCompanyVirtualHost(long companyId);

}
