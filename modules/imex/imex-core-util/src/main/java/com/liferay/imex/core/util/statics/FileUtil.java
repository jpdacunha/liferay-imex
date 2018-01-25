package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FilenameFilter;
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
	
	public static File[] listFiles(File file, String startwithPattern) throws IOException {
		
		File[] result = new File[0];
		if (file != null && file.isDirectory()) {
			result = file.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					
					return name.startsWith(startwithPattern);
					
				}
			});
		}
		return result;
		
	}

}
