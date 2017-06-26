package com.liferay.product.demo.api.converter;

import java.util.Map;
import java.util.Map.Entry;

public class JsConverter {
	
	public static String mapToJsArray(Map<String, Double> data) {
		
		String jsArray = "[";
		
		if (data != null) {
			
			int i = 0;
			for (Entry<String, Double> entry : data.entrySet()) {
				
				if (i != 0) {
					jsArray += ",";
				}
				jsArray += "[\"" + entry.getKey() + "\"," + entry.getValue() + "]";
				i++;
				
			}
			
		}
		
		jsArray += "]";
		
		return jsArray;
		
	}

}
