package com.liferay.imex.core.api.importer;

import com.liferay.imex.core.api.model.OnExistsMethodEnum;
import com.liferay.imex.core.api.model.OnMissingMethodEnum;
import com.liferay.imex.core.util.exception.ImexException;

import java.util.Properties;

public interface ImportBehaviorManagerService {
	
	public OnMissingMethodEnum getOnMissingBehavior(Properties config, String friendlyURL, String prefix) throws ImexException;

	public OnExistsMethodEnum getOnExistsBehavior(Properties config, String friendlyURL, String prefix) throws ImexException;

}
