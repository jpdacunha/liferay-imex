package com.liferay.imex.core.service.exporter.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierGeneratorImpl;

public class ExporterProcessIdentifierGenerator extends ProcessIdentifierGeneratorImpl {
	
	protected final static String IDENTIFIER = "exporter";
	
	protected final static String SMALL_IDENTIFIER = "exp";

	public ExporterProcessIdentifierGenerator() {
		super(IDENTIFIER, SMALL_IDENTIFIER);
	}

}
