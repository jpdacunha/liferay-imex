package com.liferay.imex.filesystem.trigger.service.impl;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.exporter.ImexExportService;
import com.liferay.imex.core.api.importer.ImexImportService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.core.util.statics.FileUtil;
import com.liferay.imex.filesystem.trigger.service.FilesystemTriggerService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = FilesystemTriggerService.class)
public class FileSystemTriggerServiceImpl implements FilesystemTriggerService {
	
	private static final Log _log = LogFactoryUtil.getLog(FileSystemTriggerServiceImpl.class);
		
	private static final String IMEX_FILESYSTEM_IMPORT_START_FILENAME = "imex.filesystem.import";

	private static final String IMEX_FILESYSTEM_EXPORT_START_FILENAME = "imex.filesystem.export";
	
	private static final String IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME = IMEX_FILESYSTEM_IMPORT_START_FILENAME + ".lock";

	private static final String IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME = IMEX_FILESYSTEM_EXPORT_START_FILENAME + ".lock";
	
	private static final String IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME = IMEX_FILESYSTEM_IMPORT_START_FILENAME + ".done";

	private static final String IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME = IMEX_FILESYSTEM_EXPORT_START_FILENAME + ".done";
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexExportService imexExportService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexImportService imexImportService;
	
	@Override
	public void cleanFiles() {
	
		File workDir = configurationService.getImexWorkFile();
		List<File> toClean = FileUtil.findFiles(workDir, IMEX_FILESYSTEM_IMPORT_START_FILENAME + ".*", IMEX_FILESYSTEM_EXPORT_START_FILENAME + ".*");
		
		for (File file : toClean) {
			if (file != null && file.isFile()) {
				file.delete();
			} else if (file != null) {
				_log.error("File identified by [" + file.getAbsolutePath() + "] is not a valid file. Mabe it is a directory ?");
			} else {
				_log.debug("Unable to clean null file");
			}
		}
		
	}
	
	@Override
	public synchronized void executeImex() {
		
		if (!isImportFileExists() || !isExportFileExists()) {
			_log.error("Missing required files : please check configuration and try again");
		} else {
			
			if (!isExportInProgressFileExists() && !isImportInProgressFileExists()) {
				
				boolean importStartFileExists = isImportStartFileExists();
				boolean exportStartFileExists = isExportStartFileExists();
				
				if (exportStartFileExists || importStartFileExists) {
				
					boolean isExport = false;
					String startFileName = null;
					String inprogressFileName = null;
					String waitingFileName = null;
					
					if (exportStartFileExists) {
						
						startFileName = IMEX_FILESYSTEM_EXPORT_START_FILENAME;
						waitingFileName = IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME;
						inprogressFileName = IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME;
						isExport = true;
						
					} else if (importStartFileExists) {
						
						startFileName = IMEX_FILESYSTEM_IMPORT_START_FILENAME;
						waitingFileName = IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME;
						inprogressFileName = IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME;
						isExport = false;
						
					}
					
					if (Validator.isNotNull(startFileName)) {
						
						if (Validator.isNotNull(waitingFileName)) {
							
							if (Validator.isNotNull(inprogressFileName)) {
						
								File startFile = getFile(startFileName);
								File waitingFile = getFile(waitingFileName);
								File inprogressFile = getFile(inprogressFileName);
								
								if (startFile != null && startFile.exists()) {
									//Indicates process is in progress
									startFile.renameTo(inprogressFile);
								}
								
								if (isExport) {
									imexExportService.doExportAll();
								} else {
									imexImportService.doImportAll();
								}
								
								if (inprogressFile != null && inprogressFile.exists()) {
									//Indicates process is done 
									inprogressFile.renameTo(waitingFile);
								}
								
							} else {
								_log.warn("[" + inprogressFileName + "] does not exists");
							}
							
						} else {
							_log.warn("[" + waitingFileName + "] does not exists");
						}
						
					} else {
						_log.warn("[" + startFileName + "] does not exists");
					}
					
				} else {
					_log.debug("Nothing to do : skipping execution");
				}
				
			} else {
				_log.info("Import or export is already inProgress skipping execution");
			}
			
		}
	
	}

	@Override
	public void createMissingFiles() {
		
		try {
			
			File workDir = configurationService.getImexWorkFile();
			
			if (workDir == null || !workDir.exists()) {
				throw new ImexException("Unable to initialize trigger properly because working directory is not properly initialized");
			}
			
			if (!isImportFileExists()) {
				File importLockFile = getImportWaitingFileName();
				importLockFile.createNewFile();
			} else {
				_log.debug("Lock file already exists for import");
			}
			
			if (!isExportFileExists()) {
				File exportLockFile = getExportWaitingFileName() ;
				exportLockFile.createNewFile();
			} else {
				_log.debug("Lock file already exists for export");
			}
			
		} catch (Exception e) {
			_log.error(e,e);
		}
		
	}

	
	private boolean isImportFileExists() {
		return isTriggerFileExists(IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME, IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME, IMEX_FILESYSTEM_IMPORT_START_FILENAME);
	}
	
	private boolean isExportFileExists() {
		return isTriggerFileExists(IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME, IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME, IMEX_FILESYSTEM_EXPORT_START_FILENAME);
	}
	

	private File getImportWaitingFileName() {
		return getFile(IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME);		
	}
	
	private File getExportWaitingFileName() {
		return getFile(IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME);				
	}

	private boolean isExportInProgressFileExists() {
		return isFileExists(IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME);
	}
	
	private boolean isImportInProgressFileExists() {
		return isFileExists(IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME);
	}
	
	private boolean isExportStartFileExists() {
		return isFileExists(IMEX_FILESYSTEM_EXPORT_START_FILENAME);
	}
	
	private boolean isImportStartFileExists() {
		return isFileExists(IMEX_FILESYSTEM_IMPORT_START_FILENAME);
	}
	
	private boolean isTriggerFileExists(String inProgressFileName, String waitingFileName, String startFileName) {		
		return (isFileExists(inProgressFileName) || isFileExists(waitingFileName)|| isFileExists(startFileName));	
	}
	
	private boolean isFileExists(String fileName) {
		File file = getFile(fileName);
		return (file != null && file.exists());
	}
	
	private File getFile(String fileName) {
		
		if (Validator.isNull(fileName)) {
			return null;
		}
		
		File workDir = configurationService.getImexWorkFile();
		return new File(workDir.getAbsolutePath() + StringPool.SLASH + fileName);
		
	}

}
