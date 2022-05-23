package com.liferay.imex.rest.trigger.api.comparator;

import com.liferay.imex.rest.trigger.api.dto.v1_0.ExporterDescriptor;

import java.util.Comparator;

public class ExporterDescriptorNameComparator extends AbstractStringComparator implements Comparator<ExporterDescriptor> {

	@Override
	public int compare(ExporterDescriptor arg0, ExporterDescriptor arg1) {
		return super.compare(arg0.getName(), arg1.getName());
	}


}
