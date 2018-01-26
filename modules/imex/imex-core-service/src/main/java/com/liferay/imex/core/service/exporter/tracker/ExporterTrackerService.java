package com.liferay.imex.core.service.exporter.tracker;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.util.exception.MissingKeyException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


@Component(immediate = true, service = ExporterTracker.class)
public class ExporterTrackerService implements ServiceTrackerCustomizer<Exporter, com.liferay.imex.core.api.exporter.Exporter>, ExporterTracker {
	
	private static final Log _log = LogFactoryUtil.getLog(ExporterTrackerService.class);

	private BundleContext _bundleContext;
	
	private ServiceTracker<Exporter, com.liferay.imex.core.api.exporter.Exporter> _serviceTracker;
	
	private Map<String, ServiceReference<Exporter>> _serviceReferences = new ConcurrentHashMap<>();

	@Override
	public Exporter addingService(ServiceReference<Exporter> serviceReference) {
		
		addServiceReference(serviceReference);
		
		Exporter exporter = _bundleContext.getService(serviceReference);
		
		return exporter;
	}
	
	public synchronized void addServiceReference(ServiceReference<Exporter> serviceReference) {
		
		Bundle bundle = serviceReference.getBundle();
		String key = bundle.getSymbolicName();
		_log.info("# Adding [" + key + "] ...");
		_serviceReferences.put(key, serviceReference);
		
	}

	@Override
	public void modifiedService(ServiceReference<Exporter> serviceReference, Exporter service) {
		
		removedService(serviceReference, service);
		addingService(serviceReference);
		
	}

	@Override
	public void removedService(ServiceReference<Exporter> serviceReference, Exporter service) {
		
		Bundle bundle = serviceReference.getBundle();
		String key = bundle.getSymbolicName();
		_log.info("# Removing [" + key + "] ...");
		_serviceReferences.remove(key);
		
	}
	
	@Activate
	@Modified
	protected void activate(BundleContext bundleContext) {
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}

		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(bundleContext, Exporter.class, this);

	}
	
	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTracker = null;

		_bundleContext = null;

		if (_log.isDebugEnabled()) {
			_log.debug("Deactivated");
		}
	}

	@Override
	public Map<String, ServiceReference<Exporter>> getExporters() {
		return _serviceReferences;
	}

	@Override
	public Map<String, ServiceReference<Exporter>> getFilteredExporters(List<String> bundleNames) {
		
		Map<String, ServiceReference<Exporter>> filteredServiceReferences = null;
		try {
			filteredServiceReferences = CollectionUtil.filterByKeys(bundleNames, _serviceReferences);
		} catch (MissingKeyException e) {
			_log.info(MessageUtil.getMessage("There's something wrong in your syntax : " + e.getMessage()));
		}
				
		return filteredServiceReferences;
	}


}
