package com.liferay.imex.core.service;

import com.liferay.imex.core.api.ImexConfigurationService;
import com.liferay.imex.core.util.configuration.ImExPropsValues;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = ImexConfigurationService.class)
public class ImexConfigurationServiceImpl implements ImexConfigurationService {
	
	

	private static final Log _log = LogFactoryUtil.getLog(ImexConfigurationServiceImpl.class);
	
	public final static String EXPORTER = "exporter";
	public final static String IMPORTER = "importer";
	
	private static final String DEFAULT_FILENAME_PREFIX = "default";

	@Override
	public Properties loadExporterConfiguration(Bundle bundle) {
		
		return loadConfiguration(bundle, EXPORTER);
		
	}
	
	@Override
	public Properties loadImporterConfiguration(Bundle bundle) {
		
		return loadConfiguration(bundle, IMPORTER);
		
	}
	
	private Properties loadConfiguration(Bundle bundle, String type) {
		
		Properties props = getFileSystemConfiguration(bundle, type);
		
		if (props == null) {
			props = getDefaultConfiguration(bundle);
		} 
		
		return props;
		
	}
	
	private Properties getFileSystemConfiguration(Bundle bundle, String type) {
		//TODO : JDA aller chercher les fichiers dans le répertoire deploy/imex
		return null;
	}
	
	private Properties getDefaultConfiguration(Bundle bundle) {
		
		Properties props = null;
		
		String fileName = DEFAULT_FILENAME_PREFIX + ".properties";
				
		URL fileURL = bundle.getResource(fileName);
		
		try {
			
			if (fileURL != null) {
				
				InputStream in = fileURL.openStream();
				
				props = new Properties();
				props.load(in);
				
				in.close();
				
		    } else {
		    	_log.debug("Resource [" + fileName + "] is null.");
		    }

			
		} catch (IOException e) {
			_log.error(e,e);
	    }
		
		
		if (props != null) {
			_log.info(MessageUtil.getMessage(bundle, "is using default configuration loaded from [" + fileName + "]"));
		} else {
			_log.info(MessageUtil.getMessage(bundle, "has no default configuration to loads. Make sure "));
		}
		
		return props;
		
	}

	@Override
	public String getImexPath() {
		return ImExPropsValues.DEPLOY_DIR + "/imex";
	}
	
	@Override
	public String getImexDataPath() {
		return getImexPath() + "/data";
	}

}
