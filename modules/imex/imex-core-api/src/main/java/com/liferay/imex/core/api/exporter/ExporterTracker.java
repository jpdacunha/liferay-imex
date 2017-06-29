package com.liferay.imex.core.api.exporter;

import java.util.Map;

import org.osgi.framework.ServiceReference;

public interface ExporterTracker {
	
	public Map<String, ServiceReference<Exporter>> getExporters();

}
