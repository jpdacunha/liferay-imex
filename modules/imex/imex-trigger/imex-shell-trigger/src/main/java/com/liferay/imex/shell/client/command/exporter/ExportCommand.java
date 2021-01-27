package com.liferay.imex.shell.client.command.exporter;

import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.shell.trigger.ImexCommand;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=ex",
		    "osgi.command.scope=imex"
		  }
		)
public class ExportCommand implements ImexCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExportService imexExportService;
	
	public void ex(String ... bundleNames) {
		
		String id = imexExportService.doExport(bundleNames);
		System.out.println("[" + id + "] Done.");
	
	}

}
