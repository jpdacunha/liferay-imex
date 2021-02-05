package com.liferay.imex.core.service.exporter.model;

import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.petra.string.StringPool;

public class RawExporterProcessIdentifierGeneratorWrapper implements ProcessIdentifierGenerator {
	
	private ExporterProcessIdentifierGenerator generator;

	private static final String RAW = "raw";
	
	public RawExporterProcessIdentifierGeneratorWrapper(ExporterProcessIdentifierGenerator generator) {
		super();
		this.generator = generator;
	}

	public String getOrGenerateUniqueIdentifier() {
		return RAW + StringPool.PERIOD + generator.getOrGenerateUniqueIdentifier();
	}

	public String generateProcessTypeUniqueIdentifier() {
		return RAW + StringPool.PERIOD + generator.generateProcessTypeUniqueIdentifier();
	}

	public ExporterProcessIdentifierGenerator getGenerator() {
		return generator;
	}

	@Override
	public String getProcessType() {
		return RAW + StringPool.PERIOD + generator.getProcessType();
	}

}
