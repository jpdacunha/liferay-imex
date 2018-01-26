package com.liferay.imex.shell.client.command.importer;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.liferay.imex.core.api.importer.Importer;
import com.liferay.imex.core.api.importer.ImporterTracker;
import com.liferay.imex.shell.client.ImexCommand;
import com.liferay.imex.shell.client.util.TableBuilder;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

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
	
	private final static String[] COLUMN_NAMES = {"Ranking", "Bundle Name", "Description"}; 

	public void li() {
		
		Map<String, ServiceReference<Importer>> importers = trackerService.getImporters();
		
		if (importers != null && importers.size() > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (Map.Entry<String ,ServiceReference<Importer>> entry  : importers.entrySet()) {
				
				ServiceReference<Importer> serviceReference = entry.getValue();
				
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
