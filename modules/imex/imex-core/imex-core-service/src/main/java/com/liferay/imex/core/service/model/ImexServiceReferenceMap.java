package com.liferay.imex.core.service.model;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class ImexServiceReferenceMap<T> {
	
	private static final Log _log = LogFactoryUtil.getLog(PriorizedImexServiceReferenceMap.class);

	private Map<String, ServiceReference<T>> map = Collections.synchronizedMap(new TreeMap<String, ServiceReference<T>>());

	public synchronized ServiceReference<?> addServiceReference(ServiceReference<T> serviceReference) {
		
		String key = getKey(serviceReference);
		_log.debug("Adding [" + key + "] ...");
		return map.put(key, serviceReference);
		
	}
	
	public void removeService(ServiceReference<T> serviceReference) {
		
		String key = getKey(serviceReference);
		_log.debug("Removing [" + key + "] ...");
		map.remove(key);
		
	}

	@SuppressWarnings("rawtypes")
	public static String getKey(ServiceReference serviceReference) {
		Bundle bundle = serviceReference.getBundle();
		String key = bundle.getSymbolicName();
		return key;
	}	
	
	public Map<String, ServiceReference<T>> getMap() {
		
		return map;
		
	}

}
