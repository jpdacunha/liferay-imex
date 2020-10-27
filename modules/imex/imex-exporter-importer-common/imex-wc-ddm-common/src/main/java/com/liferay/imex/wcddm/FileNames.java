package com.liferay.imex.wcddm;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;

import java.util.Locale;

public class FileNames {
	
	public static final String DIR_WCDDM = "/wcddm";
	public static final String STRUCTURE_FILENAME = "structure";
	public static final String TEMPLATE_FILENAME = "template";
	
	public static String getTemplateFileName(DDMTemplate ddmTemplate, Group group, Locale locale, String extension) {
		return getTemplateFileNameBegin() + ddmTemplate.getTemplateKey() + extension;
	}
	
	public static String getTemplateFileNameBegin() {
		return FileNames.TEMPLATE_FILENAME + StringPool.MINUS;
	}

	public static  String getStructureFileName(DDMStructure ddmStructure, Group group, Locale locale, String extension) {
		return getStructureFileNameBegin() + ddmStructure.getStructureKey() + extension;
	}
	
	public static String getStructureFileNameBegin() {
		return FileNames.STRUCTURE_FILENAME + StringPool.MINUS;
	}
	
}
