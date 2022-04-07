package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ImporterDescriptor;
import com.liferay.imex.rest.trigger.api.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class ImporterDescriptorSerDes {

	public static ImporterDescriptor toDTO(String json) {
		ImporterDescriptorJSONParser importerDescriptorJSONParser =
			new ImporterDescriptorJSONParser();

		return importerDescriptorJSONParser.parseToDTO(json);
	}

	public static ImporterDescriptor[] toDTOs(String json) {
		ImporterDescriptorJSONParser importerDescriptorJSONParser =
			new ImporterDescriptorJSONParser();

		return importerDescriptorJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ImporterDescriptor importerDescriptor) {
		if (importerDescriptor == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (importerDescriptor.getDescription() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"description\": ");

			sb.append("\"");

			sb.append(_escape(importerDescriptor.getDescription()));

			sb.append("\"");
		}

		if (importerDescriptor.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(importerDescriptor.getName()));

			sb.append("\"");
		}

		if (importerDescriptor.getPriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append(importerDescriptor.getPriority());
		}

		if (importerDescriptor.getProfiled() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profiled\": ");

			sb.append(importerDescriptor.getProfiled());
		}

		if (importerDescriptor.getRanking() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ranking\": ");

			sb.append("\"");

			sb.append(_escape(importerDescriptor.getRanking()));

			sb.append("\"");
		}

		if (importerDescriptor.getSupportedProfilesIds() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"supportedProfilesIds\": ");

			sb.append("[");

			for (int i = 0;
				 i < importerDescriptor.getSupportedProfilesIds().length; i++) {

				sb.append("\"");

				sb.append(
					_escape(importerDescriptor.getSupportedProfilesIds()[i]));

				sb.append("\"");

				if ((i + 1) <
						importerDescriptor.getSupportedProfilesIds().length) {

					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ImporterDescriptorJSONParser importerDescriptorJSONParser =
			new ImporterDescriptorJSONParser();

		return importerDescriptorJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ImporterDescriptor importerDescriptor) {

		if (importerDescriptor == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (importerDescriptor.getDescription() == null) {
			map.put("description", null);
		}
		else {
			map.put(
				"description",
				String.valueOf(importerDescriptor.getDescription()));
		}

		if (importerDescriptor.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(importerDescriptor.getName()));
		}

		if (importerDescriptor.getPriority() == null) {
			map.put("priority", null);
		}
		else {
			map.put(
				"priority", String.valueOf(importerDescriptor.getPriority()));
		}

		if (importerDescriptor.getProfiled() == null) {
			map.put("profiled", null);
		}
		else {
			map.put(
				"profiled", String.valueOf(importerDescriptor.getProfiled()));
		}

		if (importerDescriptor.getRanking() == null) {
			map.put("ranking", null);
		}
		else {
			map.put("ranking", String.valueOf(importerDescriptor.getRanking()));
		}

		if (importerDescriptor.getSupportedProfilesIds() == null) {
			map.put("supportedProfilesIds", null);
		}
		else {
			map.put(
				"supportedProfilesIds",
				String.valueOf(importerDescriptor.getSupportedProfilesIds()));
		}

		return map;
	}

	public static class ImporterDescriptorJSONParser
		extends BaseJSONParser<ImporterDescriptor> {

		@Override
		protected ImporterDescriptor createDTO() {
			return new ImporterDescriptor();
		}

		@Override
		protected ImporterDescriptor[] createDTOArray(int size) {
			return new ImporterDescriptor[size];
		}

		@Override
		protected void setField(
			ImporterDescriptor importerDescriptor, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					importerDescriptor.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					importerDescriptor.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "priority")) {
				if (jsonParserFieldValue != null) {
					importerDescriptor.setPriority(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "profiled")) {
				if (jsonParserFieldValue != null) {
					importerDescriptor.setProfiled(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "ranking")) {
				if (jsonParserFieldValue != null) {
					importerDescriptor.setRanking((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "supportedProfilesIds")) {

				if (jsonParserFieldValue != null) {
					importerDescriptor.setSupportedProfilesIds(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			Class<?> valueClass = value.getClass();

			if (value instanceof Map) {
				sb.append(_toJSON((Map)value));
			}
			else if (valueClass.isArray()) {
				Object[] values = (Object[])value;

				sb.append("[");

				for (int i = 0; i < values.length; i++) {
					sb.append("\"");
					sb.append(_escape(values[i]));
					sb.append("\"");

					if ((i + 1) < values.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}
			else {
				sb.append(String.valueOf(entry.getValue()));
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}