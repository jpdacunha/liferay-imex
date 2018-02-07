package com.liferay.imex.processor.xml;


import com.liferay.imex.core.api.processor.ImexProcessor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.Serializable;

import org.osgi.service.component.annotations.Component;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Style;

@Component(immediate = true, service = ImexProcessor.class)
public class SimpleXmlProcessor implements ImexProcessor {
	
	private static final Log _log = LogFactoryUtil.getLog(SimpleXmlProcessor.class);

	private Serializer serializer;
	
	public SimpleXmlProcessor() {
		super();
		Style style = new HyphenStyle();
		Format format = new Format(3, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", style);
		this.serializer = new Persister(format); 
	}
	
	public Object read(Class<?> source, File directory, String fileName) throws Exception {
		return (Object)serializer.read(source, new File(directory, fileName));
	}
	
	public void write(Serializable source, File directory, String fileName) throws Exception {
		serializer.write(source, new File(directory, fileName));
	}

	@Override
	public String getFileExtension() {
		return ".xml";
	}

	@Override
	public Object read(Class<?> source, File file) throws Exception {
		return (Object)serializer.read(source, file);
	}
	
}
