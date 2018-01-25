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

	

}
