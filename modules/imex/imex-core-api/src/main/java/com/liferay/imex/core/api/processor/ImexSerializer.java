package com.liferay.imex.core.api.processor;

import java.io.File;
import java.io.Serializable;

public interface ImexSerializer {
	
	public void write(Serializable source, File directory, String fileName) throws Exception;
	
	public String getFileExtension();

}
