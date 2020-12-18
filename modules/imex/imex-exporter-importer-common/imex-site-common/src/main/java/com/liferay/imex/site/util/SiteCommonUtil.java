package com.liferay.imex.site.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SiteCommonUtil {
	
	private static final Log _log = LogFactoryUtil.getLog(SiteCommonUtil.class);
	
	public static File[] manageExclusions(List<String> friendlyUrlsToExclude, File[] groupDirs) {
		
		if (groupDirs == null || groupDirs.length == 0) {
			return groupDirs;
		}
		
		if (friendlyUrlsToExclude == null || friendlyUrlsToExclude.size() == 0) {
			return groupDirs;
		}
		
		List<File> filteredResult = new ArrayList<>(Arrays.asList(groupDirs.clone()));
		
		for (String friendlyUrl : friendlyUrlsToExclude) {
			
			filteredResult.removeIf(e -> (e.getAbsolutePath().endsWith(friendlyUrl)));
		}
		
		if (filteredResult == null || filteredResult.size() == 0) {
			return null;
		} else {
			return filteredResult.toArray(new File[filteredResult.size()]);
		}
		
	}
	
	public static File[] managePriority(List<String> orderedFriendlyUrls, File[] groupDirs) {
		
		if (groupDirs == null || groupDirs.length == 0) {
			return groupDirs;
		}
		
		if (orderedFriendlyUrls == null || orderedFriendlyUrls.size() == 0) {
			return groupDirs;
		}
		
		List<File> groupDirsList = Arrays.asList(groupDirs);
		List<File> orderedResult = new ArrayList<File>();
		
		//Initialise with content of groupDirs
		List<File> missingOrder = new ArrayList<>(Arrays.asList(groupDirs.clone()));
		
		for (String friendlyUrl : orderedFriendlyUrls) {
			
			// Finding if groupFriendly URL exists in list of directories
			 List<File> filteredList = groupDirsList
				      .stream()
				      .filter(e -> e.getAbsolutePath().endsWith(friendlyUrl))
				      .collect(Collectors.toList());
			 
			if (filteredList.size() == 1) {

				orderedResult.add(filteredList.get(0));
				
				//Actual element is found => not a missing element
				missingOrder.remove(filteredList.get(0));
				
			} else if (filteredList.size() > 1) {
				_log.warn("Ambiguous value detected : more than one result is matching [" + friendlyUrl + "] in group directory list");
				
			} else {
				_log.warn("[" + friendlyUrl + "] is marqued to be excluded but it's does not exists as a directory");
			}
			
		}
		
		if (orderedResult == null || orderedResult.size() == 0) {
			return null;
		} else {
			
			//Adding missing directories to the end 
			orderedResult.addAll(missingOrder);
			
			return orderedResult.toArray(new File[orderedResult.size()]);
		}
		
	}

	public static List<Group> managePriority(List<String> friendlyUrlsToExclude, List<Group>  groups) {
		//TOOD JDA : manage priority and exclusions of imports
		return groups;
	}

	public static List<Group> manageExclusions(List<String> friendlyUrlsToExclude, List<Group> groups) {
		//TOOD JDA : manage exclusions and exclusions of imports
		return groups;
	}
	

}
