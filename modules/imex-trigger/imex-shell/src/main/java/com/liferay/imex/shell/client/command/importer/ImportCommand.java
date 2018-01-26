package com.liferay.imex.shell.client.command.importer;

import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.shell.client.ImexCommand;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=im",
		    "osgi.command.scope=imex"
		  }
		)
public class ImportCommand implements ImexCommand {
		
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexImportService imexImportService;
	
	public void im(String... bundleNames) {
		
		imexImportService.doImport(bundleNames);
		System.out.println("Done.");
	
	}

}	
