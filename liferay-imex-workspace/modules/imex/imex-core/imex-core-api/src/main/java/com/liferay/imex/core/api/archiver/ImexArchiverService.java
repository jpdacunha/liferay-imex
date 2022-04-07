package com.liferay.imex.core.api.archiver;

import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;

import java.io.File;
import java.util.Properties;

public interface ImexArchiverService {
	
	public void archiveDataDirectory(Properties coreConfig, ProcessIdentifierGenerator processIdentifier);
	
	public void archiveDataDirectory(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier);

	public void archiveCfgDirectory(Properties coreConfig, ProcessIdentifierGenerator processIdentifier);

	public void archiveCfgDirectory(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier);

	public void archiveAndClean(Properties coreConfig, File toArchiveDirectory, File archiveDestinationDirectory, ProcessIdentifierGenerator processIdentifier);

}
