package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(FileUtil.class);
	
	public static File[] listFiles(File file) throws IOException {
		
		File[] result = new File[0];
		
		if (file != null && file.isDirectory()) {
			
			File[] directoryFiles = file.listFiles();
			
			if (directoryFiles != null) {
				result = directoryFiles;
			} else {
				_log.warn("No files exists in directory [" + file.getCanonicalPath() + "]");
			}
			
		}
		return result;
		
	}

}
