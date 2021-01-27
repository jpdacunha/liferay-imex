package com.liferay.imex.shell.client.command.core;

import com.liferay.imex.shell.trigger.ImexCommand;

import org.osgi.service.component.annotations.Component;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=help",
		    "osgi.command.scope=imex"
		  }
		)
public class HelpCommand implements ImexCommand {
	
	public void help() {
		
		System.out.println("Configuration commands :");
		System.out.println("  imex:cfggen : Generate all configuration files thats allows core configuration overrides");
		System.out.println(" ");
		
		System.out.println("Export commands :");
		System.out.println("  imex:le : List available exporters");
		System.out.println("  imex:ex  : Launch export process for all available exporters");
		System.out.println("  imex:ex [Exporter_Name] : Launch export process identify by name");
		System.out.println(" ");
		
		System.out.println("Import commands :");
		System.out.println("  imex:li : List available importers");
		System.out.println("  imex:im  : Launch import process for all available importers");
		System.out.println("  imex:im [Importer_Name] : Launch import process identify by name");
		
	}

}
