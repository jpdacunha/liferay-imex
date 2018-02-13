package com.liferay.imex.core.service.archiver.impl;

import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.identifier.ProcessIdentifier;
import com.liferay.imex.core.service.archiver.util.ZipUtils;
import com.liferay.imex.core.util.enums.ImexOperationEnum;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		service = ImexArchiverService.class
	)
public class ImexArchiverServiceImpl implements ImexArchiverService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexArchiverServiceImpl.class);
	
	public final static String ARCHIVAGE_ERROR = "Archive service error";
	
	public final static String ARCHIVE_FILENAME_PREFIX = "imex.";
	public final static String ARCHIVE_FILENAME_SUFFIX = ".zip";
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	/**
	 * Archive exported files in a zip
	 * @param rootDirectory
	 * @param archiveHistory
	 */
	public void archive(Properties coreConfig, ProcessIdentifier processIdentifier) {
		
		int nbArchiveToKeep = GetterUtil.getInteger(coreConfig.get(ImExCorePropsKeys.ARCHIVE_HISTORY_NUMBER));
		archive(nbArchiveToKeep, processIdentifier);
		
	}
	
	public void archive(int nbArchiveToKeep, ProcessIdentifier processIdentifier) {
		
		_log.info(MessageUtil.getSeparator());
		_log.info(MessageUtil.getStartMessage("Archiving process"));
		_log.debug(MessageUtil.getPropertyMessage("Nb archive history to keep" , nbArchiveToKeep + ""));
		
		if (nbArchiveToKeep > 0) {
			
			File dataDirectory = new File(configurationService.getImexDataPath());
			
			if (dataDirectory != null && dataDirectory.isDirectory()) {
			
				File archiveDestinationDirectory = initializeArchiveDestinationDirectory();
				
				if (archiveDestinationDirectory != null && archiveDestinationDirectory.isDirectory()) {
					
					if (zip(dataDirectory, archiveDestinationDirectory, processIdentifier)) {
						cleanup(archiveDestinationDirectory, processIdentifier, nbArchiveToKeep);
					}
					
				} else {
					_log.error(MessageUtil.getError(ARCHIVAGE_ERROR, "Unable to archive an invalid directory"));
				}
			
			} else {
				String dne = MessageUtil.getDNE("Archive service is currently disabled");
				_log.error(MessageUtil.getError(ARCHIVAGE_ERROR, dne));
			}
			
		} else {
			_log.debug(MessageUtil.getMessage("Archive service is currently disabled"));
		}
		
		_log.info(MessageUtil.getEndMessage("Archiving process"));
		_log.info(MessageUtil.getSeparator());
		
	}
	
	private void cleanup(File destinationDir, ProcessIdentifier processIdentifier, int nbArchiveToKeep) {
		
		File[] listFiles = destinationDir.listFiles();
		
		if (listFiles.length > 0) {
			
			List<File> files = Arrays.asList(listFiles);
			
			files.stream().filter((File p) -> p.getName().contains(processIdentifier.getProcessType())).sorted(new Comparator<File>() {
				
				@Override
				public int compare(File o1, File o2) {
					int answer;
					if (o1.lastModified() == o2.lastModified()) {
						answer = 0;
					} else if (o1.lastModified() > o2.lastModified()) {
						answer = -1;
					} else {
						answer = 1;
					}
					return answer;
				}
				
			}).skip(nbArchiveToKeep)
					// to delete the file but keep the most recent <nbArchiveToKeep>
					// .forEach(x -> ((File) x).delete());
					// or display the filenames which would be deleted
					.forEach((x) -> {
						File todelete = ((File) x);	
						String fileSize = FileUtil.readableFileSize(todelete.length());
						todelete.delete();
						if (!todelete.exists()) {
							_log.info(MessageUtil.getOK("Deleting unused archive file", todelete.getName() + "(" + fileSize + ")", todelete, ImexOperationEnum.DELETE));
						} else {
							_log.error(MessageUtil.getError("Unable to delete file", todelete.getAbsolutePath()));
						}
							
					});
			
		} else {
			_log.info(MessageUtil.getMessage("No archive files to delete"));
		}
		
	}
	
	private String getArchiveFileName(File dataDirectory, ProcessIdentifier processIdentifier) {
	
		return ARCHIVE_FILENAME_PREFIX + dataDirectory.getName() + StringPool.PERIOD + processIdentifier.getUniqueIdentifier() + ARCHIVE_FILENAME_SUFFIX;
		
	}

	private boolean zip(File dataDirectory, File archiveDestinationDirectory, ProcessIdentifier processIdentifier) {
		
		ZipUtils zipUtils = new ZipUtils(_log);
		String archiveFileName = getArchiveFileName(dataDirectory, processIdentifier);
		File archiveFile = new File(archiveDestinationDirectory.getPath(), archiveFileName);
		
		try {

			long sizeOfDataDirectory = FileUtils.sizeOfDirectory(dataDirectory);
			if (sizeOfDataDirectory > 0) {
				
				 zipUtils.zipFiles(archiveFile, dataDirectory);
				 
				_log.info(MessageUtil.getOK(dataDirectory.getAbsolutePath(), archiveFile.getAbsolutePath()));
				long sizeOfArchiveFile = archiveFile.length();
				_log.info(MessageUtil.getOK(FileUtil.readableFileSize(sizeOfDataDirectory) + " successfully zipped to " + FileUtil.readableFileSize(sizeOfArchiveFile)));
				
			} else {
				_log.info(MessageUtil.getMessage(dataDirectory.getAbsolutePath() + " is empty nothing to archive"));
			}
			
		} catch (IOException e) {
			_log.error(e,e);
			_log.error(MessageUtil.getErrorMessage(e)); 
		}
		
		return (archiveFile != null && archiveFile.exists());
		
	}
	
	private File initializeArchiveDestinationDirectory() {
		
		String archivePath = configurationService.getImexArchivePath();
		
		File archiveFile = new File(archivePath);
		
		archiveFile.mkdirs();
		if (!archiveFile.exists()) {
			_log.error(MessageUtil.getError(ARCHIVAGE_ERROR, "Failed to create directory " + archiveFile));
			return null;
		} else {
			return archiveFile;
		}
		
	}		
	

}
