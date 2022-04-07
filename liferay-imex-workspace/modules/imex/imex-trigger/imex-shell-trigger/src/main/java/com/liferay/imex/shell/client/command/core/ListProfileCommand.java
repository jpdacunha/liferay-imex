package com.liferay.imex.shell.client.command.core;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.configuration.model.ImexProperties;
import com.liferay.imex.core.util.statics.CollectionUtil;
import com.liferay.imex.shell.client.util.TableBuilder;
import com.liferay.imex.shell.trigger.ImexCommand;

import java.util.Arrays;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		  immediate=true,
		  service = Object.class,
		  property = {
		    "osgi.command.function=lp",
		    "osgi.command.scope=imex"
		  }
)
public class ListProfileCommand implements ImexCommand {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	private final static String[] COLUMN_NAMES = {"Profiles"}; 
	
	public void lp() {
		
		ImexProperties config = new ImexProperties();
		configurationService.loadCoreConfiguration(config);
		Properties configAsProperties = config.getProperties();
		
		String[] supportedProfiles = CollectionUtil.getArray(configAsProperties.getProperty(ImExCorePropsKeys.MANAGES_PROFILES_LIST));
		
		if (supportedProfiles != null && supportedProfiles.length > 0) {
			
			TableBuilder tableBuilder = new TableBuilder();
			
			tableBuilder.addHeaders(COLUMN_NAMES);
			
			for (String profile : Arrays.asList(supportedProfiles)) {
				tableBuilder.addRow(profile);
			}
			
			tableBuilder.print();
			
		} else {
			System.out.println("No  supported profiles. Please see [" + ImExCorePropsKeys.MANAGES_PROFILES_LIST + "] paramater to configure profiles.");
		}
		
	}

	public ImexConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ImexConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
