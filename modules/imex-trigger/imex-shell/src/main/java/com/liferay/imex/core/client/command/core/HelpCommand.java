package com.liferay.imex.core.client.command.core;

import com.liferay.imex.core.client.ImexCommand;

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
		
		System.out.println("Export commands :");
		System.out.println("  imex:le : List available exporters");
		System.out.println("  imex:ex  : Launch export process for all available exporters");
		System.out.println(" ");
		System.out.println("Import commands :");
		System.out.println("  imex:li : List available importers");
		System.out.println("  imex:im  : Launch import process for all available importers");
		
	}

}
