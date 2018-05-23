package com.liferay.imex.core.service.identifier.impl;

import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.portal.kernel.util.StringPool;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public abstract class ProcessIdentifierGeneratorImpl implements ProcessIdentifierGenerator {
	
	private SimpleDateFormat formater =  new SimpleDateFormat("yyyyMMdd-HHmmss-SSSSSS");

	private String processType;
	
	private String smallProcessType;

	public ProcessIdentifierGeneratorImpl(String identifier, String smallProcessType) {
		super();
		this.processType = identifier;
		this.smallProcessType = smallProcessType;
	}
	
	public String generateUniqueIdentifier() {
		return smallProcessType + StringPool.PERIOD + formater.format(new Date()) + StringPool.PERIOD + RandomStringUtils.randomAlphanumeric(4);
	}
	
	public String generateProcessTypeUniqueIdentifier() {
		return processType + StringPool.PERIOD + generateUniqueIdentifier();
	}

	public String getProcessType() {
		return processType;
	}
	
}

