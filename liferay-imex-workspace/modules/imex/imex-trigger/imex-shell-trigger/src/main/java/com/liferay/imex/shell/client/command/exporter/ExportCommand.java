package com.liferay.imex.shell.client.command.exporter;

import com.liferay.imex.core.api.exporter.ImexExportService;
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
		    "osgi.command.function=ex",
		    "osgi.command.scope=imex"
		  }
		)
public class ExportCommand implements ImexCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExportService imexExportService;
	
	public void ex(String ... parameters) {
		
		ImexParams params = new ImexParams();
		try {
			
			params.parse(parameters);
			
			String id = imexExportService.doExport(params.getBundleNames(), params.getProfile(), params.isDebug());
			System.out.println("[" + id + "] Done.");
			
		} catch (UnknownParameterException e) {
			System.out.println(e.getMessage());
		}

	}

}
