package com.liferay.imex.core.api.trigger;

import java.util.Map;

import org.osgi.framework.ServiceReference;

public interface TriggerTracker {
	
	public Map<String, ServiceReference<Trigger>> getTriggers();

}
