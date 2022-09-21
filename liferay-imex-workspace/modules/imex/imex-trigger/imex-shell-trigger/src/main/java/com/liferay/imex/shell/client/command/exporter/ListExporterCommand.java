package com.liferay.imex.shell.client.command.exporter;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
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
		    "osgi.command.function=le",
		    "osgi.command.scope=imex"
		  }
)
public class ListExporterCommand implements ImexCommand {

	private static final Log _log = LogFactoryUtil.getLog(ListExporterCommand.class);
	
	private ExporterTracker trackerService;
	
	private final static String[] COLUMN_NAMES = {"Ranking", "Bundle Name", "Description", "Execution priority", "Profiled"}; 

	public void le() {
		
		Map<String, ServiceReference<Exporter>> exporters = trackerService.getPriorizedExporters();
		
		if (exporters != null && exporters.size() > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> serviceReference = entry.getValue();
				
				String ranking = (Integer)serviceReference.getProperty(OSGIServicePropsKeys.SERVICE_RANKING) + "";
				String description = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_DESCRIPTION);
				String priority = (String)serviceReference.getProperty(OSGIServicePropsKeys.IMEX_COMPONENT_EXECUTION_PRIORITY);
				
				Bundle bundle = serviceReference.getBundle();
				Exporter exporter = bundle.getBundleContext().getService(serviceReference);
				
				boolean supportProfile = exporter.isProfiled();
				
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
	protected void setExporterTracker(ExporterTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetExporterTracker(ExporterTracker trackerService) {
		this.trackerService = null;
	}
	
	@Activate
	protected void start() {
		if (trackerService == null) {
			_log.warn("Tracker is incorrectly set");
	    }
	}

}	
