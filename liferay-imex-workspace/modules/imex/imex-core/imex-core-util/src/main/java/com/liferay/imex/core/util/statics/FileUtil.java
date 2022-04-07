package com.liferay.imex.core.util.statics;

import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.osgi.framework.Bundle;

public class FileUtil {
	
	public final static String LAR_EXTENSION = ".lar";
	public final static String XML_EXTENSION = ".xml";
	public final static String ZIP_EXTENSION = ".zip";
	public final static String JSON_EXTENSION = ".json";
	public final static String PROPERTIES_EXTENSION = ".properties";
	public final static String DEFAULT_FILE_PATTERN = "*";
	
	private static final Log _log = LogFactoryUtil.getLog(FileUtil.class);
	
	public static String getExtension(File file) throws ImexException {
		
		if (file != null) {
			return FilenameUtils.getExtension(file.getName());
		} else {
			throw new ImexException("[null] is not a valid file"); 
		}
		
	}
	
	public static void setFilePermissions(File file, boolean isWritable, boolean isReadable, boolean isExecutable) {
		
		if (file != null) {
			
			if (file.exists()) {
				
				file.setExecutable(isExecutable);
				file.setReadable(isReadable);
				file.setWritable(isWritable);
				
			}  else {
				_log.error("File [" + file.getAbsolutePath() + "] does not exists");
			}
			
		} else {
			_log.error("File is null : unable to perform any action");
		}
		
	}
	
	public static boolean isEmptyDirectory(File directory) throws IOException, ImexException {
		
		if (directory != null) {
			
			Path path = directory.toPath();
		
		    if (Files.isDirectory(path)) {
		        try (Stream<Path> entries = Files.list(path)) {
		            return !entries.findFirst().isPresent();
		        }
		    } else {
		    	throw new ImexException("[" + directory.getAbsolutePath() + "] is not a directory"); 
		    }
	    
		} else {
	    	throw new ImexException("[null] is not a directory"); 
	    }
	    
	}
	
	public static void deleteDirectory(File directory) {
		
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			_log.error(e,e);
		}
		
	}
	
	public static File initializeDirectory(String filePath) throws ImexException {
		
		File file = new File(filePath);
		
		file.mkdirs();
		if (!file.exists()) {
			throw new ImexException("Unable to create directory [" + filePath + "]");
		} 
		
		return file;
		
	}
	
	public static List<File> findFiles(File directory, String... patterns) {
		
		Collection<File> collection = FileUtils.listFiles(directory, new WildcardFileFilter(patterns), null);
		
		if (collection != null) {
			return new ArrayList<>(collection);
		}
		
		return null;
		
	}
	
	public static List<URL> findBundleResources(Bundle bundle, String toCopyBundleDirectoryName, String filePatternToCopy) {
		
		if (bundle != null) {

			if (Validator.isNotNull(toCopyBundleDirectoryName)) {

				if (Validator.isNull(filePatternToCopy)) {
					filePatternToCopy = DEFAULT_FILE_PATTERN;
				}

				return Collections.list(bundle.findEntries(toCopyBundleDirectoryName, filePatternToCopy, true));

			} else {
				_log.error("Missing required parameter bundleDirectoryName");
			}

		} else {
			_log.error("Missing required parameter bundle");
		}
		return null;
		
	}

	public static void copyUrlsAsFiles(File destinationDir, List<URL> list) {
		
		if (list != null) {
			
			for (URL resourceURL : list) {
			
				try (InputStream in = resourceURL.openStream()) {
					
					//Retrieve only fileName from the originalPath
					Path originalfilePath = Paths.get(resourceURL.getFile());
					String fileName = originalfilePath.getFileName().toString();
					
					File file = new File(destinationDir, fileName);
					try (FileOutputStream out = new FileOutputStream(file)) {
						
						IOUtils.copy(in, out);
						
					}
					
				} catch (IOException e) {
					_log.error(e,e);
				}

			}
			
		} else {
			_log.debug("Nothing to copy");
		}
		
	}
	
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
