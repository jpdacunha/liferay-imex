package com.liferay.imex.core.service.configuration.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierImpl;

public class ConfigurationProcessIdentifier extends ProcessIdentifierImpl {
	
	private final static String IDENTIFIER = "configuration";

	public ConfigurationProcessIdentifier() {
		super(IDENTIFIER);
	}

}
