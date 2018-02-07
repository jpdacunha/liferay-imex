package com.liferay.imex.core.api.processor;

import java.io.File;
import java.io.Serializable;

public interface ImexProcessor {
	
	public void write(Serializable source, File directory, String fileName) throws Exception;
	
	public Object read(Class<?> source, File directory, String fileName) throws Exception;
	
	public Object read(Class<?> source, File filePath) throws Exception;
	
	public String getFileExtension();

}
