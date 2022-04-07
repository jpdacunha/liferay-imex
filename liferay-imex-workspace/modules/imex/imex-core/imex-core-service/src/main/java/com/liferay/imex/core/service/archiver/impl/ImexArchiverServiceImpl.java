package com.liferay.imex.core.service.archiver.impl;

import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;
import com.liferay.imex.core.api.report.ImexExecutionReportService;
import com.liferay.imex.core.api.report.model.ImexOperationEnum;
import com.liferay.imex.core.service.archiver.util.ZipUtils;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

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
	
	public final static String ARCHIVE_FILENAME_PREFIX = ImExCorePropsKeys.IMEX_PREFIX + ".";
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExecutionReportService reportService;
	
	@Override
	public void archiveDataDirectory(Properties coreConfig, ProcessIdentifierGenerator processIdentifier) {
		
		int nbArchiveToKeep = GetterUtil.getInteger(coreConfig.get(ImExCorePropsKeys.ARCHIVE_HISTORY_NUMBER));
		archiveDataDirectory(nbArchiveToKeep, processIdentifier);
		
	}
	
	@Override
	public void archiveDataDirectory(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier) {
		
		File dataDirectory = new File(configurationService.getImexDataPath());
		archive(dataDirectory, nbArchiveToKeep, processIdentifier);
			
	}
	
	@Override
	public void archiveCfgDirectory(Properties coreConfig, ProcessIdentifierGenerator processIdentifier) {
		
		int nbArchiveToKeep = GetterUtil.getInteger(coreConfig.get(ImExCorePropsKeys.ARCHIVE_HISTORY_NUMBER));
		archiveCfgDirectory(nbArchiveToKeep, processIdentifier);
		
	}
	
	@Override
	public void archiveCfgDirectory(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier) {
		
		File cfgDir = new File(configurationService.getImexCfgOverridePath());
		archive(cfgDir, nbArchiveToKeep, processIdentifier);	
		
	}
	
	@Override
	public void archiveAndClean(Properties coreConfig, File toArchiveDirectory, File archiveDestinationDirectory, ProcessIdentifierGenerator processIdentifier) {
		
		int nbArchiveToKeep = GetterUtil.getInteger(coreConfig.get(ImExCorePropsKeys.ARCHIVE_HISTORY_NUMBER));
		archiveAndClean(toArchiveDirectory, archiveDestinationDirectory, nbArchiveToKeep, processIdentifier);
		
	}
	
	private void archiveAndClean(File toArchiveDirectory, File archiveDestinationDirectory, int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier) {
		
		reportService.getSeparator(_log);
		reportService.getStartMessage(_log, "Archiving process");
		reportService.getPropertyMessage(_log, "Nb archive history to keep" , nbArchiveToKeep + "", 4);
		
		if (nbArchiveToKeep > 0) {
			
			if (archiveDestinationDirectory == null) {
				//Settind default archive directory if no dir provided
				archiveDestinationDirectory = initializeArchiveDestinationDirectory();
			}
			
			if (archiveDestinationDirectory != null && archiveDestinationDirectory.isDirectory()) {
				
				if (toArchiveDirectory != null && toArchiveDirectory.exists()) {
					if (zip(toArchiveDirectory, archiveDestinationDirectory, processIdentifier)) {
						cleanup(archiveDestinationDirectory, processIdentifier, nbArchiveToKeep);
					}
				} else {
					reportService.getDNE(_log, toArchiveDirectory);
				}
				
			} else {
				reportService.getError(_log, ARCHIVAGE_ERROR, "Unable to archive an invalid directory");
			}
		
		} else {
			reportService.getMessage(_log, "Archive service is currently disabled");
		}
		
		reportService.getEndMessage(_log, "Archiving process");
		reportService.getSeparator(_log);
		
	}
	

	private void archive(File toArchiveDirectory, int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier) {
		
		archiveAndClean(toArchiveDirectory, null, nbArchiveToKeep, processIdentifier);
		
	}
	
	private void cleanup(File toCleanDir, ProcessIdentifierGenerator processIdentifier, int nbArchiveToKeep) {
		
		File[] listFiles = toCleanDir.listFiles();
		
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
			// to delete the file but keep the most recent <nbArchiveToKeep>	
			}).skip(nbArchiveToKeep)
					// .forEach(x -> ((File) x).delete());
					// or display the filenames which would be deleted
					.forEach((x) -> {
						File todelete = ((File) x);	
						String fileSize = FileUtil.readableFileSize(todelete.length());
						todelete.delete();
						if (!todelete.exists()) {
							reportService.getOK(_log,"Deleting unused archive file", todelete.getName() + "(" + fileSize + ")", todelete, ImexOperationEnum.DELETE);
						} else {
							reportService.getError(_log,"Unable to delete file", todelete.getAbsolutePath());
						}
							
					});
			
		} else {
			reportService.getMessage(_log, "No archive files to delete");
		}
		
	}
	
	private String getArchiveFileName(File dataDirectory, ProcessIdentifierGenerator processIdentifier) {
	
		return ARCHIVE_FILENAME_PREFIX + dataDirectory.getName() + StringPool.PERIOD + processIdentifier.generateProcessTypeUniqueIdentifier() + FileUtil.ZIP_EXTENSION;
		
	}

	private boolean zip(File dataDirectory, File archiveDestinationDirectory, ProcessIdentifierGenerator processIdentifier) {
		
		ZipUtils zipUtils = new ZipUtils(_log);
		String archiveFileName = getArchiveFileName(dataDirectory, processIdentifier);
		File archiveFile = new File(archiveDestinationDirectory.getPath(), archiveFileName);
		
		try {

			long sizeOfDataDirectory = FileUtils.sizeOfDirectory(dataDirectory);
			if (sizeOfDataDirectory > 0) {
				
				zipUtils.zipFiles(archiveFile, dataDirectory);
				 
				reportService.getOK(_log, dataDirectory.getAbsolutePath(), archiveFile.getAbsolutePath(), "successfully archived");
				long sizeOfArchiveFile = archiveFile.length();
				reportService.getOK(_log, FileUtil.readableFileSize(sizeOfDataDirectory) + " successfully zipped to " + FileUtil.readableFileSize(sizeOfArchiveFile));
				
			} else {
				reportService.getMessage(_log, dataDirectory.getAbsolutePath() + " is empty nothing to archive");
			}
			
		} catch (IOException e) {
			_log.error(e,e);
			reportService.getError(_log, e); 
		}
		
		return (archiveFile != null && archiveFile.exists());
		
	}
	
	private File initializeArchiveDestinationDirectory() {
		
		String archivePath = configurationService.getImexArchivePath();
		
		File archiveFile = new File(archivePath);
		
		archiveFile.mkdirs();
		if (!archiveFile.exists()) {
			reportService.getError(_log, ARCHIVAGE_ERROR, "Failed to create directory " + archiveFile);
			return null;
		} else {
			return archiveFile;
		}
		
	}		
	

}
