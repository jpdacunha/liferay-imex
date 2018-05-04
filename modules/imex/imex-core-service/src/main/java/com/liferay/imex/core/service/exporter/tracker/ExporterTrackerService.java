package com.liferay.imex.core.service.exporter.tracker;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.service.model.ImexServiceReferenceMap;
import com.liferay.imex.core.util.exception.MissingKeyException;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


@Component(immediate = true, service = ExporterTracker.class)
public class ExporterTrackerService implements ServiceTrackerCustomizer<Exporter, com.liferay.imex.core.api.exporter.Exporter>, ExporterTracker {
	
	private static final Log _log = LogFactoryUtil.getLog(ExporterTrackerService.class);

	private BundleContext _bundleContext;
	
	private ServiceTracker<Exporter, com.liferay.imex.core.api.exporter.Exporter> _serviceTracker;
	
	private ImexServiceReferenceMap<Exporter> _serviceReferences = new ImexServiceReferenceMap<Exporter>();
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public Exporter addingService(ServiceReference<Exporter> serviceReference) {
		
		_serviceReferences.addServiceReference(serviceReference);
		
		Exporter exporter = _bundleContext.getService(serviceReference);
		
		return exporter;
	}

	@Override
	public void modifiedService(ServiceReference<Exporter> serviceReference, Exporter service) {
		
		removedService(serviceReference, service);
		addingService(serviceReference);
		
	}

	@Override
	public void removedService(ServiceReference<Exporter> serviceReference, Exporter service) {
		_serviceReferences.removeService(serviceReference);
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
		return _serviceReferences.getMap();
	}

	@Override
	public Map<String, ServiceReference<Exporter>> getFilteredExporters(List<String> bundleNames) {
		
		Map<String, ServiceReference<Exporter>> filteredServiceReferences = null;
		try {
			filteredServiceReferences = CollectionUtil.filterByKeys(bundleNames, getExporters());
		} catch (MissingKeyException e) {
			reportService.getMessage(_log, "There's something wrong in your syntax : " + e.getMessage());
		}
				
		return filteredServiceReferences;
	}


}
