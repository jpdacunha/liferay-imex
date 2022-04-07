package com.liferay.imex.shell.client.command.core;

import com.liferay.imex.core.api.ImexCoreService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=cfgmer",
		    "osgi.command.scope=imex"
		  }
		)
public class MergeCfgCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexCoreService imexService;

}
