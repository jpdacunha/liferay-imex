package com.liferay.imex.core.service.configuration.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierImpl;

public class ConfigurationOverrideProcessIdentifier extends ProcessIdentifierImpl {
	
	private final static String IDENTIFIER = "cfg.override";

	public ConfigurationOverrideProcessIdentifier() {
		super(IDENTIFIER);
	}

}
