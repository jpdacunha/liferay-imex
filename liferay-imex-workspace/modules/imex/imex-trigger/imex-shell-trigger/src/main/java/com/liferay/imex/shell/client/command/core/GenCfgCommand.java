package com.liferay.imex.shell.client.command.core;

import com.liferay.imex.core.api.ImexCoreService;
import com.liferay.imex.shell.trigger.ImexCommand;

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
public class GenCfgCommand implements ImexCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexCoreService imexService;
	
	
	public void cfggen(String ... bundleNames) {
		
		String id = imexService.generateOverrideFileSystemConfigurationFiles();
		System.out.println("[" + id + "] Done.");
		
	}

}
