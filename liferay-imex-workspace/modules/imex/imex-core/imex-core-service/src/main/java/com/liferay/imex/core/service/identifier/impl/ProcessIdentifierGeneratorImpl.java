package com.liferay.imex.core.service.identifier.impl;

import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.petra.string.StringPool;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public abstract class ProcessIdentifierGeneratorImpl implements ProcessIdentifierGenerator {
	
	private SimpleDateFormat formater =  new SimpleDateFormat("yyyyMMdd-HHmmss-SSSSSS");

	private String processTypeDescription;
	
	private String smallProcessTypeDescription;
	
	private String uniqueIdentifier;
	
	private String processTypeUniqueIdentifier;

	public ProcessIdentifierGeneratorImpl(String identifier, String smallProcessType) {
		super();
		this.processTypeDescription = identifier;
		this.smallProcessTypeDescription = smallProcessType;
	}
	
	public String getOrGenerateUniqueIdentifier() {
		
		if (uniqueIdentifier == null) {
			uniqueIdentifier = smallProcessTypeDescription + StringPool.PERIOD + formater.format(new Date()) + StringPool.PERIOD + RandomStringUtils.randomAlphanumeric(4);
		}
		
		return uniqueIdentifier;
		
	}
	
	public String generateProcessTypeUniqueIdentifier() {
		
		if (processTypeUniqueIdentifier == null) {
			processTypeUniqueIdentifier = processTypeDescription + StringPool.PERIOD + getOrGenerateUniqueIdentifier();
		}
		return processTypeUniqueIdentifier;
		
	}

	public String getProcessType() {
		return processTypeDescription;
	}
	
}

