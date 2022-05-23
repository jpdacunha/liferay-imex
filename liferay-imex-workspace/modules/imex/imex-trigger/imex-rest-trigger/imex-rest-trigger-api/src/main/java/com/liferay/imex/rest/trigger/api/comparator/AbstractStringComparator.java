package com.liferay.imex.rest.trigger.api.comparator;

public abstract class AbstractStringComparator {
	
	public static int compare(String name1, String name2) {

		if (name1 == name2) {
			return 0;
		}
		if (name1 == null) {
			return -1;
		}
		if (name2 == null) {
			return 1;
		}
		return name1.compareToIgnoreCase(name2);
	}

}
