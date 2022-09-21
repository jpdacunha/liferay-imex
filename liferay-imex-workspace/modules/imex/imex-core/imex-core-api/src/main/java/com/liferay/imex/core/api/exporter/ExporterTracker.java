package com.liferay.imex.core.api.exporter;

import java.util.List;
import java.util.Map;

import org.osgi.framework.ServiceReference;

public interface ExporterTracker {
	
	public Map<String, ServiceReference<Exporter>> getPriorizedExporters();
	
	public Map<String, ServiceReference<Exporter>> getFilteredExporters(List<String> bundleNames);

}
