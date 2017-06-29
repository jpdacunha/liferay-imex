package com.liferay.imex.core.client.command.exporter;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.liferay.imex.core.api.exporter.Exporter;
import com.liferay.imex.core.api.exporter.ExporterTracker;
import com.liferay.imex.core.client.ImexCommand;
import com.liferay.imex.core.client.util.TableBuilder;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=le",
		    "osgi.command.scope=imex"
		  }
)
public class ListExporterCommand extends ExportTrackerCommand implements ImexCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(ListExporterCommand.class);
	
	private ExporterTracker trackerService;
	
	private final static String[] COLUMN_NAMES = {"Ranking", "Bundle Name", "Description"}; 

	public void le() {
		
		Map<String, ServiceReference<Exporter>> exporters = trackerService.getExporters();
		
		if (exporters != null && exporters.size() > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (Map.Entry<String ,ServiceReference<Exporter>> entry  : exporters.entrySet()) {
				
				ServiceReference<Exporter> serviceReference = entry.getValue();
				
				String priority = (Integer)serviceReference.getProperty("service.ranking") + "";
				String description = (String)serviceReference.getProperty("imex.component.description");
				//TODO : JDA vérifier que c pas null
				Bundle bundle = serviceReference.getBundle();
				
				if (bundle != null) {
					tableBuilder.addRow(priority, bundle.getSymbolicName(), description);
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
			_log.error("Tracker is incorrectly set");
	    }
	}

}	
