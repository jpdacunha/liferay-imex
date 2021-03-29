package com.liferay.imex.shell.client.command.trigger;

import com.liferay.imex.core.api.trigger.Trigger;
import com.liferay.imex.core.api.trigger.TriggerTracker;
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
		    "osgi.command.function=lt",
		    "osgi.command.scope=imex"
		  }
)
public class ListTriggerCommand implements ImexCommand {
	
	private static final Log _log = LogFactoryUtil.getLog(ListTriggerCommand.class);
	
	private TriggerTracker trackerService;
	
	private final static String[] COLUMN_NAMES = {"Ranking", "Bundle Name", "Descriptive name", "Description"}; 

	public void lt() {
		
		Map<String, ServiceReference<Trigger>> triggers = trackerService.getTriggers();
		
		if (triggers != null && triggers.size() > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (Map.Entry<String ,ServiceReference<Trigger>> entry  : triggers.entrySet()) {
				
				ServiceReference<Trigger> serviceReference = entry.getValue();
			
				String ranking = (Integer)serviceReference.getProperty(OSGIServicePropsKeys.SERVICE_RANKING) + "";
				Bundle bundle = serviceReference.getBundle();
				Trigger trigger = bundle.getBundleContext().getService(serviceReference);
				
				String description = trigger.getTriggerDescription();
				String descriptiveName = trigger.getTriggerName();
				
				if (bundle != null) {
					tableBuilder.addRow(ranking, bundle.getSymbolicName(), descriptiveName, description);
				}
				
			}
			
			tableBuilder.print();
					
		} else {
			System.out.println("No IMEX triggers registered.");
		}

	
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
	protected void setImporterTracker(TriggerTracker trackerService) {
		this.trackerService = trackerService;
	}

	protected void unsetImporterTracker(TriggerTracker trackerService) {
		this.trackerService = null;
	}
	
	@Activate
	protected void start() {
		if (trackerService == null) {
			_log.warn("Tracker is incorrectly set");
	    }
	}

}	
