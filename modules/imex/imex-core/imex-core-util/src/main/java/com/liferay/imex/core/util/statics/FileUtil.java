package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileUtil {
	
	public final static String LAR_EXTENSION = ".lar";
	
	private static final Log _log = LogFactoryUtil.getLog(FileUtil.class);
	
	public static File[] listFiles(File file) {
		
		File[] result = new File[0];
		
		if (file != null && file.isDirectory()) {
			
			File[] directoryFiles = file.listFiles();
			
			if (directoryFiles != null) {
				result = directoryFiles;
			} else {
				try {
					_log.warn("No files exists in directory [" + file.getCanonicalPath() + "]");
				} catch (IOException e) {
					_log.error(e,e);
				}
			}
			
		}
		return result;
		
	}
	
	public static File[] listFiles(File file, String startwithPattern) {
		
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
	
	public static String readableFileSize(long size) {
		
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	    
	}
	
	public static void isValidDirectory(File destinationDir) throws SystemException {
		
		if (destinationDir == null) {
			throw new SystemException("Invalid null destination directory");
		}
		
		if (!destinationDir.exists()) {
			throw new SystemException("[" + destinationDir.getAbsolutePath() + "] does not exists");
		}
		
		if (!destinationDir.isDirectory()) {
			throw new SystemException("[" + destinationDir.getAbsolutePath() + "] is not a directory");
		}
		
	}

}
