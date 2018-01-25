package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.util.StringPool;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class ImexNormalizer {
	
	/**
	 * Replace alla spécia chars by a minus
	 * @param s
	 * @return
	 */
	public static String convertToKey(String s) {
		
		if (s != null) {
			s = s.toLowerCase();
			s = Normalizer.normalize(s, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			s = s.replaceAll("[^a-zA-Z0-9]+", StringPool.MINUS);
		}
		return s;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String getDirNameByFriendlyURL(String s) {
		
		//Remove first slashes of urls
		if (s != null && s.startsWith(StringPool.SLASH)) {
			s = s.replaceAll(StringPool.SLASH, StringPool.BLANK);
		}
		return s;
		
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String getFriendlyURLByDirName(String s) {
		
		//Remove first slashes of urls
		if (s != null && !s.startsWith(StringPool.SLASH)) {
			s = StringPool.SLASH + s;
		}
		return s;
		
	}
	

}
