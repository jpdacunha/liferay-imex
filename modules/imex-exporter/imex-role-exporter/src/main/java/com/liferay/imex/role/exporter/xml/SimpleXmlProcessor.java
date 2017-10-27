package com.liferay.imex.role.exporter.xml;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Style;

public class SimpleXmlProcessor<T> {

	private Class<T> clazz;
	private File file;
	private Serializer serializer;
	
	public SimpleXmlProcessor(Class<T> clazz, File directory, String fileName) {
		super();
		Style style = new HyphenStyle();
		Format format = new Format(3, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", style);
		this.serializer = new Persister(format); 
		this.clazz = clazz;
		this.file = new File(directory, fileName);
	}
	
	public T read() throws Exception {
		T result = serializer.read(clazz, file);
		return result;
	}
	
	public void write(T source) throws Exception {
		serializer.write(source, file);
	}

	
	
}
