package com.liferay.imex.rest.trigger.api.internal.resource.v1_0;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.api.profile.ImexProfileService;
import com.liferay.imex.core.util.configuration.OSGIServicePropsKeys;
import com.liferay.imex.rest.trigger.api.comparator.ImporterDescriptorNameComparator;
import com.liferay.imex.rest.trigger.api.dto.v1_0.ImporterDescriptor;
import com.liferay.imex.rest.trigger.api.resource.v1_0.ImportersResource;
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
	properties = "OSGI-INF/liferay/rest/v1_0/importers.properties",
	scope = ServiceScope.PROTOTYPE, service = ImportersResource.class
)
public class ImportersResourceImpl extends BaseImportersResourceImpl {
	
	private static final Log _log = LogFactoryUtil.getLog(ImportersResourceImpl.class);
	
	private ImporterTracker trackerService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private ImexProfileService imexProfileService;

	@Override
	public Page<ImporterDescriptor> getImportersPage() throws Exception {
		
		List<ImporterDescriptor> descriptors = new ArrayList<>();
		
		Map<String, ServiceReference<Importer>> importers = trackerService.getImporters();
		
		if (importers != null && importers.size() > 0) {
			
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> serviceReference = entry.getValue();
				Bundle bundle = serviceReference.getBundle();
				Importer importer = bundle.getBundleContext().getService(serviceReference);
				
				String ranking = (Integer)serviceReference.getProperty(OSGIServicePropsKeys.SERVICE_RANKING) + "";
				String description = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_DESCRIPTION);
				String priority = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
				String name = bundle.getSymbolicName();
				boolean profiled = importer.isProfiled();
				
				ImporterDescriptor descriptor = new ImporterDescriptor();
				descriptor.setDescription(description);
				
				descriptor.setName(name);
				descriptor.setPriority(Integer.valueOf(priority));
				descriptor.setProfiled(profiled);
				descriptor.setRanking(ranking);
				
				String[] supportedProfilesIds = {};
				if (profiled) {
					supportedProfilesIds = imexProfileService.getSupportedProfilesIds();	
					descriptor.setSupportedProfilesIds(supportedProfilesIds);
				}
				
				descriptors.add(descriptor);
				
				Collections.sort(descriptors, new ImporterDescriptorNameComparator());
				
			}
			
		} else {
			_log.debug("No registered importers");
		}
		
		return Page.of(descriptors);
		
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(ImporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetImporterTracker(ImporterTracker trackerService) {
		this.trackerService = null;
	}
	
	
	@Activate
	protected void start() {
		if (trackerService == null) {
			_log.warn("Tracker is incorrectly set");
	    }
	}

	public ImexProfileService getImexProfileService() {
		return imexProfileService;
	}

	public void setImexProfileService(ImexProfileService imexProfileService) {
		this.imexProfileService = imexProfileService;
	}
	
}