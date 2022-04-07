package com.liferay.imex.core.service.trigger.tracker;

import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.trigger.Trigger;
import com.liferay.imex.core.api.trigger.TriggerTracker;
import com.liferay.imex.core.service.model.ImexServiceReferenceMap;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

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


@Component(immediate = true, service = TriggerTracker.class)
public class TriggerTrackerService implements ServiceTrackerCustomizer<Trigger, com.liferay.imex.core.api.trigger.Trigger>, TriggerTracker {
	
	private static final Log _log = LogFactoryUtil.getLog(TriggerTrackerService.class);

	private BundleContext _bundleContext;
	
	private ServiceTracker<Trigger, com.liferay.imex.core.api.trigger.Trigger> _serviceTracker;
	
	private ImexServiceReferenceMap<Trigger> _serviceReferences = new ImexServiceReferenceMap<Trigger>();
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;

	@Override
	public Trigger addingService(ServiceReference<Trigger> serviceReference) {
		
		_serviceReferences.addServiceReference(serviceReference);
		
		Trigger trigger = _bundleContext.getService(serviceReference);
		
		//Calling deploy method on custom service
		trigger.deploy();
		
		return trigger;
	}

	@Override
	public void modifiedService(ServiceReference<Trigger> serviceReference, Trigger service) {
		
		removedService(serviceReference, service);
		addingService(serviceReference);
		
	}

	@Override
	public void removedService(ServiceReference<Trigger> serviceReference, Trigger service) {
		
		_serviceReferences.removeService(serviceReference);
		
		//Calling undeploy custom method on service
		service.undeploy();
		
	}
	
	@Activate
	@Modified
	protected void activate(BundleContext bundleContext) {
		
		if (_serviceTracker != null) {
			_serviceTracker.close();
		}

		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(bundleContext, Trigger.class, this);

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
	public Map<String, ServiceReference<Trigger>> getTriggers() {
		return _serviceReferences.getMap();
	}

}
