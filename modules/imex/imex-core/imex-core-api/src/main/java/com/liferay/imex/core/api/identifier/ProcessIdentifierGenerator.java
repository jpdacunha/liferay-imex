package com.liferay.imex.core.api.identifier;

public interface ProcessIdentifierGenerator {
	
	public String getOrGenerateUniqueIdentifier();
	
	public String getProcessType();
	
	public String generateProcessTypeUniqueIdentifier();

}
