package com.liferay.imex.shell.client.command.model;

import com.liferay.imex.core.api.configuration.ImExCorePropsKeys;
import com.liferay.imex.shell.client.command.model.exception.UnknownParameterException;

import java.util.ArrayList;
import java.util.List;

public class ImexParams {
	
	public final static String IMPORTER_SUFFIX = "importer";
	public final static String EXPORTER_SUFFIX = "exporter";		
	
	private List<String> bundleNames;
	private boolean isDebug;
	private String profile;
	
	public void parse(String[] parameters) throws UnknownParameterException {
		
		List<String> bundleNames = new ArrayList<String>();
		String profileId = null;
		boolean isDebug = false;
		
		for (String parameter : parameters) {
			
			parameter = parameter.trim();
			
			String profilePrefixedParameter = CommandOptionsEnum.getPrefixedParameter(CommandOptionsEnum.PROFILE);
			String debugPrefixedParameter = CommandOptionsEnum.getPrefixedParameter(CommandOptionsEnum.DEBUG);
			
			if (parameter.startsWith(profilePrefixedParameter)) {
				profileId = parameter.replaceFirst(profilePrefixedParameter, "");
			} else if (parameter.startsWith(debugPrefixedParameter)) {
				isDebug = true;
			} else if (parameter.startsWith(ImExCorePropsKeys.IMEX_PREFIX) && (parameter.endsWith(IMPORTER_SUFFIX) || parameter.endsWith(EXPORTER_SUFFIX))) {
				bundleNames.add(parameter);
			} else {
				throw new UnknownParameterException("Unknown option [" + parameter + "]");
			}
			
		}
		
		this.bundleNames = bundleNames;
		this.profile = profileId;
		this.isDebug = isDebug;
		
	}

	public List<String> getBundleNames() {
		return bundleNames;
	}
	public void setBundleNames(List<String> bundleNames) {
		this.bundleNames = bundleNames;
	}
	public boolean isDebug() {
		return isDebug;
	}
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	

}
