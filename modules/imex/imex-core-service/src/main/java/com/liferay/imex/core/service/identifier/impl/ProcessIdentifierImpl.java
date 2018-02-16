package com.liferay.imex.core.service.identifier.impl;

import com.liferay.imex.core.api.identifier.ProcessIdentifier;
import com.liferay.portal.kernel.util.StringPool;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

public abstract class ProcessIdentifierImpl implements ProcessIdentifier {
	
	private SimpleDateFormat formater =  new SimpleDateFormat("yyyyMMdd-HHmmss-SSSSSS");

	private String processType;

	public ProcessIdentifierImpl(String identifier) {
		super();
		this.processType = identifier;
	}
	
	public String getUniqueIdentifier() {
		return formater.format(new Date()) + StringPool.PERIOD + RandomStringUtils.randomAlphanumeric(8);
	}
	
	public String getProcessTypeUniqueIdentifier() {
		return processType + StringPool.PERIOD + getUniqueIdentifier();
	}

	public String getProcessType() {
		return processType;
	}
	
}

