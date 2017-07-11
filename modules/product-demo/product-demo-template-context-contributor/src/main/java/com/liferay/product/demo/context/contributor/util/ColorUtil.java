package com.liferay.product.demo.context.contributor.util;

import java.awt.Color;

import com.liferay.portal.kernel.util.Validator;

public class ColorUtil {
	
	public static String darker(String colorHexa, int strength) {
				
		if (Validator.isNotNull(colorHexa)) {
			
			Color color = Color.decode(colorHexa);
			
			for(int i=1; i<=strength; i++){
				color = color.darker();
			}
			
			String hex = Integer.toHexString(color.getRGB());
			return "#" + hex.substring(2, hex.length());
		}
		
		return colorHexa;
		
	}
	
	
	public static String lighter(String colorHexa, int strength) {
		
		if (Validator.isNotNull(colorHexa)) {
			
			Color color = Color.decode(colorHexa);
			
			for(int i=1; i<=strength; i++){
				color = color.brighter();
			}
			
			String hex = Integer.toHexString(color.getRGB());
			return "#" + hex.substring(2, hex.length());
		}
		
		return colorHexa;
		
	}
	

}
