package com.liferay.imex.site.importer.service;

import com.liferay.imex.site.model.OnExistsSiteMethodEnum;
import com.liferay.imex.site.model.OnMissingSiteMethodEnum;
import com.liferay.portal.kernel.model.Group;

import java.util.Properties;

public interface ImportSiteBehaviorManagerService {

	public OnMissingSiteMethodEnum getOnMissingBehavior(Properties config, String friendlyURL);

	public OnExistsSiteMethodEnum getOnExistsBehavior(Properties config, Group group);

}
