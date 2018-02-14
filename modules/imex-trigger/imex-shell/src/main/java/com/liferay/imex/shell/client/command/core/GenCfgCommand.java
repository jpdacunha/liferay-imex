package com.liferay.imex.shell.client.command.core;

import com.liferay.imex.core.api.ImexService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=cfggen",
		    "osgi.command.scope=imex"
		  }
		)
public class GenCfgCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexService imexService;
	
	
	public void cfggen(String ... bundleNames) {
		
		imexService.generateOverrideFileSystemConfigurationFiles();
		System.out.println("Done.");
		
	}

}
