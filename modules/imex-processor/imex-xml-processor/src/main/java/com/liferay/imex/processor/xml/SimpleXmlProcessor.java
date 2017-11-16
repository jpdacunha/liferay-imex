package com.liferay.imex.processor.xml;


import com.liferay.imex.core.api.processor.ImexSerializer;
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

@Component(immediate = true, service = ImexSerializer.class)
public class SimpleXmlProcessor implements ImexSerializer {
	
	private static final Log _log = LogFactoryUtil.getLog(SimpleXmlProcessor.class);

	private Serializer serializer;
	
	public SimpleXmlProcessor() {
		super();
		Style style = new HyphenStyle();
		Format format = new Format(3, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", style);
		this.serializer = new Persister(format); 
	}
	
	/*public Serializable read(Class<Serializable> clazz, File directory, String fileName) throws Exception {
		Serializable result = serializer.read(clazz, new File(directory, fileName));
		return result;
	}*/
	
	public void write(Serializable source, File directory, String fileName) throws Exception {
		serializer.write(source, new File(directory, fileName));
	}

	@Override
	public String getFileExtension() {
		return ".xml";
	}
	
}
