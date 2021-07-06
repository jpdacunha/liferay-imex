package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.rest.trigger.api.resource.v1_0.ReportsResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author jpdacunha
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/reports.properties",
	scope = ServiceScope.PROTOTYPE, service = ReportsResource.class
)
public class ReportsResourceImpl extends BaseReportsResourceImpl {
}