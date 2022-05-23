package com.liferay.imex.rest.trigger.api.comparator;

import com.liferay.imex.rest.trigger.api.dto.v1_0.ImporterDescriptor;

import java.util.Comparator;

public class ImporterDescriptorNameComparator extends AbstractStringComparator implements Comparator<ImporterDescriptor> {

	@Override
	public int compare(ImporterDescriptor arg0, ImporterDescriptor arg1) {
		return super.compare(arg0.getName(), arg1.getName());
	}

}
