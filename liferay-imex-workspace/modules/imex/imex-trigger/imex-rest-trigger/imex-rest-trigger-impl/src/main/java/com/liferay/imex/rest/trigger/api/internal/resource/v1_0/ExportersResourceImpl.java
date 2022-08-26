package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.api.profile.ImexProfileService;
import com.liferay.imex.core.util.configuration.OSGIServicePropsKeys;
import com.liferay.imex.rest.trigger.api.comparator.ExporterDescriptorNameComparator;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ExporterDescriptor;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ExportersResource;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.vulcan.pagination.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author jpdacunha
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/exporters.properties",
	scope = ServiceScope.PROTOTYPE, service = ExportersResource.class
)
public class ExportersResourceImpl extends BaseExportersResourceImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(ExportersResourceImpl.class);
	
	private ExporterTracker trackerService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexCoreService imexCoreService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexProfileService imexProfileService;

	@Override
	public Page<ExporterDescriptor> getExportersPage() throws Exception {
		
		List<ExporterDescriptor> descriptors = new ArrayList<>();
		
		Map<String, ServiceReference<Exporter>> exporters = trackerService.getExporters();
		
		if (exporters != null && exporters.size() > 0) {
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> serviceReference = entry.getValue();
				Bundle bundle = serviceReference.getBundle();
				Exporter exporter = bundle.getBundleContext().getService(serviceReference);
				
				String ranking = (Integer)serviceReference.getProperty(OSGIServicePropsKeys.SERVICE_RANKING) + "";
				String description = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_DESCRIPTION);
				String priority = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
				String name = bundle.getSymbolicName();
				boolean profiled = exporter.isProfiled();
				
				ExporterDescriptor descriptor = new ExporterDescriptor();
				descriptor.setDescription(description);
				descriptor.setName(name);
				descriptor.setPriority(Integer.valueOf(priority));
				descriptor.setRanking(ranking);

				descriptor.setProfiled(profiled);
				
				String[] supportedProfilesIds = {};
				if (profiled) {
					supportedProfilesIds =	imexProfileService.getSupportedProfilesIds();					
				}
				descriptor.setSupportedProfilesIds(supportedProfilesIds);
				
				descriptors.add(descriptor);
				
				Collections.sort(descriptors, new ExporterDescriptorNameComparator());
				
			}
			
		} else {
			_log.debug("No registered exporters");
		}
		
		return Page.of(descriptors);
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.trackerService = null;
	}

	public ImexCoreService getImexCoreService() {
		return imexCoreService;
	}

	public void setImexCoreService(ImexCoreService imexCoreService) {
		this.imexCoreService = imexCoreService;
	}

	@Activate
	protected void start() {
		if (trackerService == null) {
			_log.warn("Tracker is incorrectly set");
	    }
	}
	
}