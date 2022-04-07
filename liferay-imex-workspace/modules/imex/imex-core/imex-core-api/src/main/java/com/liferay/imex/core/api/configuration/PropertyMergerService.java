package com.liferay.imex.core.api.configuration;

import com.liferay.imex.core.api.configuration.model.FileProperty;

import java.io.File;

public interface PropertyMergerService {
	
	public void merge(FileProperty sourceFileProperty, FileProperty modifiedFileProperty, File outputFile);

}
