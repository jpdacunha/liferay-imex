package com.liferay.imex.core.util.statics;

import com.liferay.imex.core.util.exception.MissingKeyException;
import com.liferay.portal.kernel.log.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
	
	/**
	 * Filter a collection by keys. If a key is not found in the map an exception is throws.
	 * @param keys
	 * @return
	 * @throws MissingKeyException
	 */
	public static <K> Map<String, K> filterByKeys(List<String> keys, Map<String, K> toBefiltred) throws MissingKeyException {
		
		Map<String,K> filteredServiceReferences = new HashMap<String, K>();
		
		boolean exists = true;
		
		for (String name : keys) {
			
			exists = toBefiltred.get(name) != null;
			if (exists) {
				filteredServiceReferences.put(name, toBefiltred.get(name));
			} else {
				throw new MissingKeyException("Undefined value [" + name + "]");
			}
			
		}
	
		return filteredServiceReferences;
		
	}
	
	public static <K> void printKeys (Map<String, K> map, Log logger) {
		
		if (map != null && map.size() > 0) {
			logger.info(MessageUtil.getMessage("Available keys :"));
		}
		
		for (String key : map.keySet()) {
			logger.info(MessageUtil.getMessage(key, 1));
		}
		
	}
	
	
}
