package com.liferay.imex.core.service.importer.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierGeneratorImpl;

public class ImporterProcessIdentifier extends ProcessIdentifierGeneratorImpl {

	private final static String IDENTIFIER = "importer";
	
	private final static String SMALL_IDENTIFIER = "imp";

	public ImporterProcessIdentifier() {
		super(IDENTIFIER, SMALL_IDENTIFIER);
	}

}
