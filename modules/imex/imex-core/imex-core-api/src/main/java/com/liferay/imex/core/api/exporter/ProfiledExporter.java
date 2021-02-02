package com.liferay.imex.core.api.exporter;

public interface ProfiledExporter extends Exporter {
	
	@Override
	default public boolean isProfiled() {
		return true;
	}

}
