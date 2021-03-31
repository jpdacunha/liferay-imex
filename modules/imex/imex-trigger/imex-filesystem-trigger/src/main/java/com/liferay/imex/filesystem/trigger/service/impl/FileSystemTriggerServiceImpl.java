package com.liferay.imex.filesystem.trigger.service.impl;

import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.util.exception.ImexException;
import com.liferay.imex.filesystem.trigger.service.FilesystemTriggerService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(immediate = true, service = FilesystemTriggerService.class)
public class FileSystemTriggerServiceImpl implements FilesystemTriggerService {
	
	private static final Log _log = LogFactoryUtil.getLog(FileSystemTriggerServiceImpl.class);
	
	private static final String IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME = "imex.filesystem.import";

	private static final String IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME = "imex.filesystem.export";
	
	private static final String IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME = IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME + ".done";

	private static final String IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME = IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME + ".done";

	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
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
		return isTriggerFileExists(IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME, IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME);
	}
	
	private boolean isExportFileExists() {
		return isTriggerFileExists(IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME, IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME);
	}
	
	private File getImportInProgressFileName() {
		File workDir = configurationService.getImexWorkFile();
		return new File(workDir.getAbsolutePath() + "/" + IMEX_FILESYSTEM_IMPORT_INPROGRESS_FILENAME);
	}
	
	private File getExportInProgressFileName() {
		File workDir = configurationService.getImexWorkFile();
		return new File(workDir.getAbsolutePath() + "/" + IMEX_FILESYSTEM_EXPORT_INPROGRESS_FILENAME);		
	}
	
	private File getImportWaitingFileName() {
		File workDir = configurationService.getImexWorkFile();
		return new File(workDir.getAbsolutePath() + "/" + IMEX_FILESYSTEM_IMPORT_WAITING_FILENAME);		
	}
	
	private File getExportWaitingFileName() {
		File workDir = configurationService.getImexWorkFile();
		return new File(workDir.getAbsolutePath() + "/" + IMEX_FILESYSTEM_EXPORT_WAITING_FILENAME);				
	}
	
	private boolean isTriggerFileExists(String inProgressFileName, String waitingFileName) {
		
		File workDir = configurationService.getImexWorkFile();
		File inProgressFile = new File(workDir.getAbsolutePath() + StringPool.SLASH + inProgressFileName);
		File waitingFile = new File(workDir.getAbsolutePath() + StringPool.SLASH + waitingFileName);
		
		return (inProgressFile.exists() || waitingFile.exists());
		
	}

}
