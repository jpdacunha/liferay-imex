package com.liferay.imex.core.util.statics;

import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.osgi.framework.Bundle;

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
			
			return roleNames.contains(name);
			
		} else {
			_log.warn("Some of imput params are null");
		}
	
		return false;
	}
	
	/**
	 * Display humane readable properties
	 * @param props
	 * @param bundle
	 */
	public static void displayProperties(Properties props, Bundle bundle) {
        
		if (props != null) {
				
			for (Entry<Object,Object> value : props.entrySet()) {
				_log.info(MessageUtil.getMessage(":> " + value.getKey() + " = " + value.getValue(),3));
			}
			
		} 
	                               
	}

}
