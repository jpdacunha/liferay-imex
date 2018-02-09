package com.liferay.imex.core.service.importer.model;

import com.liferay.imex.core.service.identifier.impl.ProcessIdentifierImpl;

public class ImporterProcessIdentifier extends ProcessIdentifierImpl {

	private final static String IDENTIFIER = "importer";
	
	public ImporterProcessIdentifier() {
		super(IDENTIFIER);
	}

}
