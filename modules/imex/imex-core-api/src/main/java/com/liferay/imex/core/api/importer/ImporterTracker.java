package com.liferay.imex.core.api.importer;

import java.util.Map;

import org.osgi.framework.ServiceReference;

public interface ImporterTracker {
	
	public Map<String, ServiceReference<Importer>> getImporters();

}
