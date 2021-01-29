package com.liferay.imex.shell.client.command.exporter;

import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.shell.trigger.ImexCommand;

import java.util.ArrayList;
import java.util.List;

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
	
	public void ex(String ... parameters) {
		
		String profileId = null;
		List<String> bundleNames = new ArrayList<String>();
		
		for (String parameter : parameters) {
			
			parameter = parameter.trim();
			
			if (parameter.startsWith(PROFILE_ARG)) {
				profileId = parameter.replaceFirst(PROFILE_ARG, "");
			} else {
				bundleNames.add(parameter);
			}
			
		}
		
		String id = imexExportService.doExport(bundleNames, profileId);
		System.out.println("[" + id + "] Done.");
	
	}

}
