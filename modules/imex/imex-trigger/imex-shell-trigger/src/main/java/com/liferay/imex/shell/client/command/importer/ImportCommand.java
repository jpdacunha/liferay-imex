package com.liferay.imex.shell.client.command.importer;

import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.shell.client.command.model.ImexParams;
import com.liferay.imex.shell.client.command.model.exception.UnknownParameterException;
import com.liferay.imex.shell.trigger.ImexCommand;

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
	
	public void im(String... parameters) {
		
		ImexParams params = new ImexParams();
		try {
			
			params.parse(parameters);
			String id = imexImportService.doImport(params.getBundleNames(), params.getProfile(), params.isDebug());
			System.out.println("[" + id + "] Done.");
			
		} catch (UnknownParameterException e) {
			System.out.println(e.getMessage());
		}
	
	}

}	
