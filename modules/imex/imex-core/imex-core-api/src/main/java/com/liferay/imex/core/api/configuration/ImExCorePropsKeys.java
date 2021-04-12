package com.liferay.imex.core.api.configuration;

public interface ImExCorePropsKeys {
	
	public final static String IMEX_PREFIX = "imex";
	
	public final static String DEPLOYER_READ_PERMISSION_FILES_EXTENSIONS = IMEX_PREFIX + ".deployer.read.permission.files.extensions";
	
	public final static String DEPLOYER_WRITE_PERMISSION_FILES_EXTENSIONS = IMEX_PREFIX + ".deployer.write.permission.files.extensions";
	
	public final static String DEPLOYER_EXECUTE_PERMISSION_FILES_EXTENSIONS = IMEX_PREFIX + ".deployer.execute.permission.files.extensions";
	
	public final static String RAW_CONTENT_EXPORTER_ENABLED = IMEX_PREFIX + ".raw.content.exporter.enabled";
	
	public final static String DEFAULT_PROFILE_NAME = IMEX_PREFIX + ".default.profile.name";
	
	public final static String MANAGES_PROFILES_LIST = IMEX_PREFIX + ".managed.profiles.list";
	
	public final static String ARCHIVE_HISTORY_NUMBER = IMEX_PREFIX + ".archive.history.number";
	
	public final static String DISPLAY_EXECUTION_IN_LIFERAY_LOGS = IMEX_PREFIX + ".display.execution.in.liferay.logs";
	
	public final static String PERMISSIONS_SUPPORTED_ROLES = IMEX_PREFIX + ".permissions.batch.roles";
	
	public final static String RULE_PREFIX_KEY = IMEX_PREFIX + ".permissions.batch";
	
	public final static String RULE_SUFIX_KEY = "actions";
	
	public final static String REINIT_SUFIX_KEY = "reinit";

}
