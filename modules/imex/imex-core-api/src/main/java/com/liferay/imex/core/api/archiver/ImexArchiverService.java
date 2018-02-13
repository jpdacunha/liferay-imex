package com.liferay.imex.core.api.archiver;

import com.liferay.imex.core.api.identifier.ProcessIdentifier;

import java.util.Properties;

public interface ImexArchiverService {
	
	public void archive(Properties coreConfig, ProcessIdentifier processIdentifier);
	
	public void archive(int nbArchiveToKeep, ProcessIdentifier processIdentifier);

}
