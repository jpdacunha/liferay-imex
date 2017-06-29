package com.liferay.imex.core.client.command.exporter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.liferay.imex.core.api.ImexExportService;
import com.liferay.imex.core.client.ImexCommand;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=ex",
		    "osgi.command.scope=imex"
		  }
		)
public class ExportCommand extends ExportTrackerCommand implements ImexCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExportService imexExportService;
	
	public void ex() {
		
		imexExportService.doExport();
		System.out.println("Done.");
	
	}

}
