package com.liferay.imex.virtualhost.service.impl;

import com.liferay.imex.virtualhost.service.VirtualhostCommonService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.service.VirtualHostLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service = VirtualhostCommonService.class)
public class VirtualhostCommonServiceImpl implements VirtualhostCommonService {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected VirtualHostLocalService virtualHostLocalService;
	
	@Override
	public List<VirtualHost> getCompanyVirtualHost(long companyId) {
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(VirtualHost.class, classLoader)
	            .add(RestrictionsFactoryUtil.eq("companyId", companyId));
		
		
		return virtualHostLocalService.dynamicQuery(dynamicQuery);
		
		
	}
	
	@Override
	public List<VirtualHost> getLayoutSetIdVirtualHosts(long companyId, long layoutSetId) {
		
		ClassLoader classLoader = getClass().getClassLoader();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(VirtualHost.class, classLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("companyId", companyId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("layoutSetId", layoutSetId));

		return virtualHostLocalService.dynamicQuery(dynamicQuery);
	}

	public VirtualHostLocalService getVirtualHostLocalService() {
		return virtualHostLocalService;
	}

	public void setVirtualHostLocalService(VirtualHostLocalService virtualHostLocalService) {
		this.virtualHostLocalService = virtualHostLocalService;
	}

}
