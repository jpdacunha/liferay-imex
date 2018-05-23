package com.liferay.imex.core.api.identifier;

public interface ProcessIdentifierGenerator {
	
	public String generateUniqueIdentifier();
	
	public String getProcessType();
	
	public String generateProcessTypeUniqueIdentifier();

}
