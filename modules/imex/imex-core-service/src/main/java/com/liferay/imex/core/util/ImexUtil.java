package com.liferay.imex.core.util;

import com.liferay.imex.core.api.ImexService;
import com.liferay.portal.kernel.util.StringUtil;

public class ImexUtil {
	
	public static String formatDefaultBundleName(ImexService service) {
		
		if (service == null) {
			return null;
		}
		
		Class<?> clazz = service.getClass();

		return StringUtil.replace(clazz.getName(), new char[] {'.', '$'}, new char[] {'_', '_'});
		
	}

}
