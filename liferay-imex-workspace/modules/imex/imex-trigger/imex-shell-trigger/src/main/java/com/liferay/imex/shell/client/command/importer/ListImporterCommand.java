package com.liferay.imex.shell.client.command.importer;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.core.util.configuration.OSGIServicePropsKeys;
import com.liferay.imex.shell.client.util.TableBuilder;
import com.liferay.imex.shell.trigger.ImexCommand;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=li",
		    "osgi.command.scope=imex"
		  }
)
public class ListImporterCommand implements ImexCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(ListImporterCommand.class);
	
	private ImporterTracker trackerService;
	
	private final static String[] COLUMN_NAMES = {"Ranking", "Bundle Name", "Description", "Execution priority", "Profiled"}; 

	public void li() {
		
		Map<String, ServiceReference<Importer>> importers = trackerService.getPriorizedImporters();
		
		if (importers != null && importers.size() > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> serviceReference = entry.getValue();
				
				String ranking = (Integer)serviceReference.getProperty(OSGIServicePropsKeys.SERVICE_RANKING) + "";
				String description = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_DESCRIPTION);
				String priority = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
				
				Bundle bundle = serviceReference.getBundle();
				Importer importer = bundle.getBundleContext().getService(serviceReference);
				
				boolean supportProfile = importer.isProfiled();
				
				if (bundle != null) {
					tableBuilder.addRow(ranking, bundle.getSymbolicName(), description, priority, supportProfile + "");
				}
				
			}
			
			tableBuilder.print();
					
		} else {
			System.out.println("No IMEX importers registered.");
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
			_log.warn("Tracker is incorrectly set");
	    }
	}

}	
