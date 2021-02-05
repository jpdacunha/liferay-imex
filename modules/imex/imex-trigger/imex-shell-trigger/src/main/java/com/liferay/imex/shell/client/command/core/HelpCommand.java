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
		
		System.out.println("Core commands :");
		System.out.println("  imex:cfggen : Generate all configuration files thats allows core configuration overrides");
		System.out.println("  imex:lp : List available profiles");
		System.out.println(" ");
		
		System.out.println("Export commands :");
		System.out.println("  imex:le : List available exporters");
		System.out.println("  imex:ex : Launch export process identify by name");
		System.out.println("    Available options :");
		System.out.println("       > [Exporter_Name_list] : Specify a list of exporter names. If missing a full export will be executed");
		System.out.println("       > -P[Profile_ID] : Execute export process for specified id. example : imex:ex -Pdev");
		System.out.println(" ");
		
		System.out.println("Import commands :");
		System.out.println("  imex:li : List available importers");
		System.out.println("  imex:im  : Launch import process for all available importers");
		System.out.println("    Available options :");
		System.out.println("       > [Importer_Name_list] : Specify a list of importer names. If missing a full import will be executed");
		System.out.println("       > -P[Profile_ID] : Execute import process for specified id. example : imex:im -Pdev");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("If you want to use a profile ");
		
	}

}
