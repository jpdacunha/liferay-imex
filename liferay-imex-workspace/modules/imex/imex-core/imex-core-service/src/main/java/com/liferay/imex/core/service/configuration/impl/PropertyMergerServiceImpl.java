package com.liferay.imex.core.service.configuration.impl;

import com.liferay.imex.core.api.configuration.PropertyMergerService;
import com.liferay.imex.core.api.configuration.model.FileProperty;
import com.liferay.imex.core.api.configuration.model.OrderedProperties;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = PropertyMergerService.class)
public class PropertyMergerServiceImpl implements PropertyMergerService {
	
	private static final String EQUAL = StringPool.EQUAL;

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	private static final Log log = LogFactoryUtil.getLog(PropertyMergerServiceImpl.class);
	
	public void merge(FileProperty sourceFileProperty, FileProperty modifiedFileProperty, File outpuFile) {
		
		if (outpuFile == null || !outpuFile.exists()) {
			log.error("Invalid outputfile");
			return;
		}
		
		reportService.getStartMessage(log, "properties merge");
		
		File sourceFile = sourceFileProperty.getOriginalFile();
		File modifiedFile = modifiedFileProperty.getOriginalFile();	
		
		reportService.getMessage(log, "Merging ORIGINAL : [" + sourceFile.getAbsolutePath() + "] with MODIFIED : [" + modifiedFile.getAbsolutePath() + "]");
		
		OrderedProperties modifiedProperties = modifiedFileProperty.getProperties();			
		OrderedProperties sourceFileProperties = sourceFileProperty.getProperties();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)))) {
				
			List<String> toWriteList = new ArrayList<>();
			String line = null;
			
			while ((line = br.readLine()) != null) {
				
				if(log.isDebugEnabled()) {
					log.debug(" # Processing line [" + line +"] ...");
				}
				
				String trimedLine = line.trim();
				boolean isComment = trimedLine.startsWith("#");
				boolean isEmpty = Validator.isNull(trimedLine);
				
				if(!isEmpty && !isComment){
					
					//search key of the line 
					String[] keyNVal = line.split(EQUAL);
					
					if (keyNVal != null && StringUtils.countMatches(line, EQUAL) == 1) {
						
						//Saving original key to preserve indentation in final property file
						String originalKey = keyNVal[0];
						String key = originalKey.trim();
						
				    	String modifiedValue = modifiedProperties.getProperty(key);
				    	String sourceValue = sourceFileProperties.getProperty(key);
				    	
				    	if (log.isDebugEnabled()) {
				    		log.debug("Processing key [" + key + "] with sourceValue:[" + sourceValue + "] versus modifiedValue:[" + modifiedValue + "] ...");
				    	}
				    	
				    	String relevantValue =  StringPool.BLANK;
				    	
				    	if (Validator.isNotNull(modifiedValue)) {
				    		
				    		relevantValue = modifiedValue;
				    		
				    	} else if (Validator.isNotNull(sourceValue)) {
				    		
				    		relevantValue = sourceValue;
				    
			    		} else {
			    			
			    			relevantValue = StringPool.BLANK;
			    			
			    		}
				    		
				    	toWriteList.add(originalKey + EQUAL + relevantValue);
				    	reportService.getMessage(log, "Registering : [" + key + "] = [" + relevantValue + "] as merged line.");
				    	
				    	
					} else {
						toWriteList.add(line);
						reportService.getError(log, "Property parsing error", "Unable to merge line [" + line + "] because it is not a valid property line. Writing unmerged line.");
					}
					
				} else {
					//Writing original line to preserve final file indentation
					toWriteList.add(line);
				}
					    	
		    }
			
			//Finally writing lines
			if (toWriteList.size() > 0) {
				log.debug("  : Writing [" + toWriteList.size() + "] lines in [" + outpuFile.getAbsolutePath() + "]");
				FileUtils.writeLines(outpuFile, StandardCharsets.UTF_8.name() ,toWriteList);
				log.debug("  : Done.");
			}
			
		} catch (IOException e) {
			log.error(e, e);
		}
		
		reportService.getEndMessage(log, "properties merge");
		
	}
	
}
