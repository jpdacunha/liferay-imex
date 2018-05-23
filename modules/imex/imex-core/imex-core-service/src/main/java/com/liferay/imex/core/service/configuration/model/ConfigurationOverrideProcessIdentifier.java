package com.liferay.imex.core.service.configuration.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierGeneratorImpl;

public class ConfigurationOverrideProcessIdentifier extends ProcessIdentifierGeneratorImpl {
	
	private final static String IDENTIFIER = "cfg.override";
	
	private final static String SMALL_IDENTIFIER = "cfg.override";

	public ConfigurationOverrideProcessIdentifier() {
		super(IDENTIFIER, SMALL_IDENTIFIER);
	}

}
