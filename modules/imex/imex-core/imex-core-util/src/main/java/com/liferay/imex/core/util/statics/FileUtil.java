package com.liferay.imex.core.util.statics;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileUtil {
	
	public final static String LAR_EXTENSION = ".lar";
	public final static String XML_EXTENSION = ".xml";
	public final static String ZIP_EXTENSION = ".zip";
	public final static String JSON_EXTENSION = ".json";
	public final static String PROPERTIES_EXTENSION = ".properties";
	
	private static final Log _log = LogFactoryUtil.getLog(FileUtil.class);
	
	public static void loadPropertiesInFile(File file, Properties props) {
		
		if (file != null) {
			
			try (FileOutputStream out = new FileOutputStream(file)) {
			
				props.store(out, null);
				
			} catch (FileNotFoundException e) {
				_log.error(e,e);
			} catch (IOException e) {
				_log.error(e,e);
			}
			
		} else {
			_log.error("Unable to load properties because file is null");
		}
		
	}
	
	public static File[] listFilesByExtension(File directory, String extension) {
		
		File[] files = listFiles(directory);
		
		if (Validator.isNull(extension)) {
			return files;
		} else {
			_log.debug("No extension provided returning all files");
		}
		
		if (files != null ) {
			
			List<File> filesList = Arrays.asList(files);
			
			Predicate<File> byExtension = file -> file.getName().endsWith(extension);
			
			List<File> result = filesList.stream().filter(byExtension).collect(Collectors.toList());
			
			File[] resultArray = new File[result.size()];
			
			return result.toArray(resultArray);
			
		} else {
			return files;
		}

		
	}
	
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
