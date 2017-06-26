package com.liferay.imex.core.service.importer.tracker;

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

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


@Component(immediate = true, service = ImporterTracker.class)
public class ImporterTrackerService implements ServiceTrackerCustomizer<Importer, com.liferay.imex.core.api.importer.Importer>, ImporterTracker {
	
	private static final Log _log = LogFactoryUtil.getLog(ImporterTrackerService.class);

	private BundleContext _bundleContext;
	
	private ServiceTracker<Importer, com.liferay.imex.core.api.importer.Importer> _serviceTracker;
	
	private Map<String, ServiceReference<Importer>> _serviceReferences = new ConcurrentHashMap<>();;

	@Override
	public Importer addingService(ServiceReference<Importer> serviceReference) {
		
		addServiceReference(serviceReference);
		
		Importer importer = _bundleContext.getService(serviceReference);
		
		return importer;
	}
	
	public synchronized void addServiceReference(ServiceReference<Importer> serviceReference) {
		
		Bundle bundle = serviceReference.getBundle();
		String key = bundle.getSymbolicName();
		_log.info("# Adding [" + key + "] ...");
		_serviceReferences.put(key, serviceReference);
		
	}

	@Override
	public void modifiedService(ServiceReference<Importer> serviceReference, Importer service) {
		
		removedService(serviceReference, service);
		addingService(serviceReference);
		
	}

	@Override
	public void removedService(ServiceReference<Importer> serviceReference, Importer service) {
		
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

		_serviceTracker = ServiceTrackerFactory.open(_bundleContext, Importer.class, this);

		if (_log.isInfoEnabled()) {
			_log.info("Activated");
		}
	}
	
	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTracker = null;

		_bundleContext = null;

		if (_log.isInfoEnabled()) {
			_log.info("Deactivated");
		}
	}

	@Override
	public Map<String, ServiceReference<Importer>> getImporters() {
		return _serviceReferences;
	}


}
