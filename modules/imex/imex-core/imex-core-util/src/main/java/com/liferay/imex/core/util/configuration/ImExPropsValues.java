package com.liferay.imex.core.util.configuration;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;

public class ImExPropsValues {
	
	public static final String LIFERAY_HOME = GetterUtil.getString(PropsUtil.get("liferay.home"));

	public static final String DEPLOY_DIR = GetterUtil.getString(PropsUtil.get(ImExPropsKeys.DEPLOY_DIR), LIFERAY_HOME + "/tools");

	public static final boolean ENABLED = GetterUtil.getBoolean(PropsUtil.get(ImExPropsKeys.ENABLED), true);
	
}
