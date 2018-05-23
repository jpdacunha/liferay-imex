package com.liferay.imex.core.service.importer.tracker;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
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


@Component(immediate = true, service = ImporterTracker.class)
public class ImporterTrackerService implements ServiceTrackerCustomizer<Importer, com.liferay.imex.core.api.importer.Importer>, ImporterTracker {
	
	private static final Log _log = LogFactoryUtil.getLog(ImporterTrackerService.class);

	private BundleContext _bundleContext;
	
	private ServiceTracker<Importer, com.liferay.imex.core.api.importer.Importer> _serviceTracker;
	
	private ImexServiceReferenceMap<Importer> _serviceReferences = new ImexServiceReferenceMap<Importer>();
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public Importer addingService(ServiceReference<Importer> serviceReference) {
		
		_serviceReferences.addServiceReference(serviceReference);
		
		Importer importer = _bundleContext.getService(serviceReference);
		
		return importer;
	}
	
	@Override
	public void modifiedService(ServiceReference<Importer> serviceReference, Importer service) {		
		removedService(serviceReference, service);
		addingService(serviceReference);
	}

	@Override
	public void removedService(ServiceReference<Importer> serviceReference, Importer service) {		
		_serviceReferences.removeService(serviceReference);
	}
	
	@Activate
	@Modified
	protected void activate(BundleContext bundleContext) {
		
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}

		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(_bundleContext, Importer.class, this);
		
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
	public Map<String, ServiceReference<Importer>> getImporters() {
		return _serviceReferences.getMap();
	}
	
	@Override
	public Map<String, ServiceReference<Importer>> getFilteredImporters(List<String> bundleNames) {
		
		Map<String, ServiceReference<Importer>> filteredServiceReferences = null;
		try {
			filteredServiceReferences = CollectionUtil.filterByKeys(bundleNames, getImporters());
		} catch (MissingKeyException e) {
			reportService.getMessage(_log, "There's something wrong in your syntax : " + e.getMessage());
		}
				
		return filteredServiceReferences;
		
	}
	
}
