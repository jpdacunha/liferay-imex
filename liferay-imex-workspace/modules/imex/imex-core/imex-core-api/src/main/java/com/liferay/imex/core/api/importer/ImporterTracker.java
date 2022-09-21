package com.liferay.imex.core.api.importer;

import java.util.List;
import java.util.Map;

import org.osgi.framework.ServiceReference;

public interface ImporterTracker {
	
	public Map<String, ServiceReference<Importer>> getPriorizedImporters();
	
	public Map<String, ServiceReference<Importer>> getFilteredImporters(List<String> bundleNames);

}
