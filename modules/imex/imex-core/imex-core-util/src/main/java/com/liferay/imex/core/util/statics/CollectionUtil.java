package com.liferay.imex.core.util.statics;

import com.liferay.imex.core.util.exception.MissingKeyException;
import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionUtil {
	
	/**
	 * Replabe nulls in map
	 * @param <T>
	 * @param <K>
	 * @param map
	 * @param defaultValue
	 * @return
	 */
	public static <T, K> Map<K, T> replaceNullValues(Map<K, T> map, T defaultValue) {

		// Replace the null value
		map = map.entrySet().stream().map(entry -> {
			if (entry.getValue() == null)
				entry.setValue(defaultValue);
			return entry;
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		return map;
	}

	/**
	 * Filter a collection by keys. If a key is not found in the map an exception is throws.
	 * @param keys
	 * @return
	 * @throws MissingKeyException
	 */
	public static <K> Map<String, K> filterByKeys(List<String> keys, Map<String, K> toBefiltred) throws MissingKeyException {
		
		Map<String,K> filteredServiceReferences = new HashMap<String, K>();
		
		if (keys != null && keys.size() > 0) {
			
			boolean exists = true;
			
			for (String name : keys) {
				
				exists = toBefiltred.get(name) != null;
				if (exists) {
					filteredServiceReferences.put(name, toBefiltred.get(name));
				} else {
					throw new MissingKeyException("Undefined value [" + name + "]");
				}
				
			}
			
		} else {
			filteredServiceReferences.putAll(toBefiltred);
		}
	
		return filteredServiceReferences;
		
	}
	
	public static List<String> getList(String value) {
		
		List<String> retour = new ArrayList<String>();
		if (value != null && value.contains(StringPool.COMMA)) {
			
			String[] array = Arrays.stream(value.split(StringPool.COMMA)).map(String::trim).toArray(String[]::new);
			
			if (array != null) {
				retour.addAll(Arrays.asList(array));
			}
			
		} else {
			retour.add(value);
		}
		return retour;
	}
	
	public static String[] getArray(String value) {
		//TODO : JDA non optimized code array => List => array (again)
		List<String> list = getList(value);
		return list.stream().toArray(String[]::new);
	}
	
}
