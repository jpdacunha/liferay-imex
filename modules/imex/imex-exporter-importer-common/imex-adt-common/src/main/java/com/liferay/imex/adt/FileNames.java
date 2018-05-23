package com.liferay.imex.adt;


import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Locale;

public class FileNames {
	
	public static final String DIR_ADT = "/adt";
	public static final String ADT_FILENAME = "adt";
	
	public static String getAdtFileName(DDMTemplate ddmTemplate, Group group, Locale locale, String extension) {
		return getAdtFileNameBegin() + ddmTemplate.getTemplateKey() + extension;
	}
	
	public static String getAdtFileNameBegin() {
		return FileNames.ADT_FILENAME + StringPool.MINUS;
	}

}
