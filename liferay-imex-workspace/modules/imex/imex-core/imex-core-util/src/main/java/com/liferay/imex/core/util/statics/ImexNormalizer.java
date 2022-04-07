package com.liferay.imex.core.util.statics;

import com.liferay.petra.string.StringPool;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class ImexNormalizer {
	
	/**
	 * Replace all spécial chars by a minus
	 * @param s
	 * @return
	 */
	public static String convertToKey(String s) {
		
		if (s != null) {
			s = s.toLowerCase();
			//Remove special chars
			s = Normalizer.normalize(s, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			s = s.replaceAll("[^a-zA-Z0-9_]+", StringPool.MINUS);
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
		
		//Add first slashes of urls
		if (s != null && !s.startsWith(StringPool.SLASH)) {
			s = StringPool.SLASH + s;
		}
		return s;
		
	}

}
