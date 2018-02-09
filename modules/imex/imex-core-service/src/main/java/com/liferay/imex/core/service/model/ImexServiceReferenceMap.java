package com.liferay.imex.core.service.model;

import com.liferay.imex.core.util.configuration.OSGIServicePropsKeys;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class ImexServiceReferenceMap<T> {

	private static final Log _log = LogFactoryUtil.getLog(ImexServiceReferenceMap.class);

	private Map<String, ServiceReference<T>> map = Collections.synchronizedMap(new TreeMap<String, ServiceReference<T>>());
	
	public synchronized ServiceReference<?> addServiceReference(ServiceReference<T> serviceReference) {
		
		String key = ImexServiceReferenceMap.getKey(serviceReference);
		_log.debug(MessageUtil.getMessage("Adding [" + key + "] ..."));
		return map.put(key, serviceReference);
		
	}
	
	public void removeService(ServiceReference<T> serviceReference) {
		
		String key = ImexServiceReferenceMap.getKey(serviceReference);
		_log.debug(MessageUtil.getMessage("Removing [" + key + "] ..."));
		map.remove(key);
		
	}

	@SuppressWarnings("rawtypes")
	public static String getKey(ServiceReference serviceReference) {
		Bundle bundle = serviceReference.getBundle();
		String key = bundle.getSymbolicName();
		return key;
	}	

	public class ImexServiceReferenceComparator implements Comparator<ServiceReference<T>> {

		@Override
		public int compare(ServiceReference<T> s1, ServiceReference<T> s2) {
			
			String priority1 = (String)s1.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
			String priority2 = (String)s2.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
			
			int order1 = 0;
			if (Validator.isNotNull(priority1) && Validator.isDigit(priority1)) {
				order1 = Integer.valueOf(priority1);
			} else {
				_log.error(MessageUtil.getError("Invalid property value", OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY + " is invalid for " + ImexServiceReferenceMap.getKey(s1)));
			}
			
			int order2 = 0;
			if (Validator.isNotNull(priority2) && Validator.isDigit(priority2)) {
				order2 = Integer.valueOf(priority2);
			} else {
				_log.error(MessageUtil.getError("Invalid property value", OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY + " is invalid for " + ImexServiceReferenceMap.getKey(s2)));
			}

			return order2-order1;

		}

	}
	
	public Map<String, ServiceReference<T>> getMap() {
		
		List<ServiceReference<T>> list = new ArrayList<>();
		for (Entry<String, ServiceReference<T>> entry : map.entrySet()) {			
			list.add(entry.getValue());			
		}
		Collections.sort(list, new ImexServiceReferenceComparator());
		
		Map<String, ServiceReference<T>> copiedMap = new LinkedHashMap<>();
		
		for (ServiceReference<T> serviceReference : list) {
			copiedMap.put(ImexServiceReferenceMap.getKey(serviceReference), serviceReference);			
		}
		
		return copiedMap;
		
	}
	
}