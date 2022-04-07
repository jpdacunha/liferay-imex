package com.liferay.imex.core.api.importer;

public interface ProfiledImporter extends Importer {
	
	@Override
	default public boolean isProfiled() {
		return true;
	}

}
