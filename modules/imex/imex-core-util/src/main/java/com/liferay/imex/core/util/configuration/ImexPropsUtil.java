package com.liferay.imex.core.util.configuration;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.List;

public class ImexPropsUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexPropsUtil.class);
	
	/**
	 * Convert comma separated list to String
	 * @param list
	 * @return
	 */
	public static List<String> getList(String list) {
		
		if (Validator.isNotNull(list)) {
			return Arrays.asList(list.split(StringPool.COMMA));
		} else {
			_log.warn("Imput param is null");
		}
		
		return null;
		
	}
	
	/**
	 * Return true if value is contained in a comma delimited string
	 * @param name
	 * @return
	 */
	public static boolean contains(String name, String list) {
		
		if (Validator.isNotNull(name) && Validator.isNotNull(list)) {
			
			List<String> roleNames = getList(list);
			
			return !roleNames.contains(name);
			
		} else {
			_log.warn("Some of imput params are null");
		}
	
		return false;
	}

}
