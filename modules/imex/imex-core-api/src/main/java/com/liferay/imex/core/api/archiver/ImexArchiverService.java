package com.liferay.imex.core.api.archiver;

import com.liferay.imex.core.api.identifier.ProcessIdentifier;

import java.util.Properties;

public interface ImexArchiverService {
	
	public void archiveData(Properties coreConfig, ProcessIdentifier processIdentifier);
	
	public void archiveData(int nbArchiveToKeep, ProcessIdentifier processIdentifier);

	public void archiveCfg(Properties coreConfig, ProcessIdentifier processIdentifier);

	public void archiveCfg(int nbArchiveToKeep, ProcessIdentifier processIdentifier);

}
