package com.liferay.imex.core.service.exporter.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierImpl;

public class ExporterProcessIdentifier extends ProcessIdentifierImpl {
	
	private final static String IDENTIFIER = "exporter";

	public ExporterProcessIdentifier() {
		super(IDENTIFIER);
	}

}
