package com.liferay.imex.core.client.command.importer;

import java.util.Map;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.client.ImexCommand;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=im",
		    "osgi.command.scope=imex"
		  }
		)
public class ImportCommand implements ImexCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(ImportCommand.class);
	
	protected ImporterTracker trackerService;
	
	public void im() {
		
		System.out.println(":> Starting import : please view logs for status");
		
		Map<String, ServiceReference<Importer>> importers = trackerService.getImporters();
		
		for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
			
			ServiceReference<Importer> reference = entry.getValue();
			
			Importer importer = reference.getBundle().getBundleContext().getService(reference);
			
			importer.doImport();
			
		}
		
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
			_log.error("Tracker is incorrectly set");
	    }
	}

}	
