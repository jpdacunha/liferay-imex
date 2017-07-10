package com.liferay.product.demo.context.contributor.util;

import java.awt.Color;

public class ColorUtil {
	
	public static String darker(String colorHexa, int strength) {
				
		Color color = Color.decode(colorHexa);
		
		for(int i=1; i<=strength; i++){
			color.darker();
		}
		
		String hex = Integer.toHexString(color.getRGB());
		return "#" + hex.substring(2, hex.length());
		
	}
	
	
	public static String lighter(String color, int strength) {
		return null;
	}
	

}
