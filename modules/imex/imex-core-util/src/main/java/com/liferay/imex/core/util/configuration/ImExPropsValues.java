package com.liferay.imex.core.util.configuration;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

public class ImExPropsValues {

	public static final String DEPLOY_DIR = GetterUtil.getString(PropsUtil.get(ImExPropsKeys.DEPLOY_DIR), PropsUtil.get("auto.deploy.deploy.dir") + "/imex");

	public static final boolean ENABLED = GetterUtil.getBoolean(PropsUtil.get(ImExPropsKeys.ENABLED), true);
	
}
