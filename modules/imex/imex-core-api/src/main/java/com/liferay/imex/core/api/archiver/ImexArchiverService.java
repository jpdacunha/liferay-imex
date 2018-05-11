package com.liferay.imex.core.api.archiver;

import com.liferay.imex.core.api.identifier.ProcessIdentifierGenerator;

import java.util.Properties;

public interface ImexArchiverService {
	
	public void archiveData(Properties coreConfig, ProcessIdentifierGenerator processIdentifier);
	
	public void archiveData(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier);

	public void archiveCfg(Properties coreConfig, ProcessIdentifierGenerator processIdentifier);

	public void archiveCfg(int nbArchiveToKeep, ProcessIdentifierGenerator processIdentifier);

}
