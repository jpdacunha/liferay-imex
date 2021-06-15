package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ImportProcess;
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
public class ImportProcessSerDes {

	public static ImportProcess toDTO(String json) {
		ImportProcessJSONParser importProcessJSONParser =
			new ImportProcessJSONParser();

		return importProcessJSONParser.parseToDTO(json);
	}

	public static ImportProcess[] toDTOs(String json) {
		ImportProcessJSONParser importProcessJSONParser =
			new ImportProcessJSONParser();

		return importProcessJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ImportProcess importProcess) {
		if (importProcess == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (importProcess.getDebug() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"debug\": ");

			sb.append(importProcess.getDebug());
		}

		if (importProcess.getImporterNames() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"importerNames\": ");

			sb.append("[");

			for (int i = 0; i < importProcess.getImporterNames().length; i++) {
				sb.append("\"");

				sb.append(_escape(importProcess.getImporterNames()[i]));

				sb.append("\"");

				if ((i + 1) < importProcess.getImporterNames().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (importProcess.getProfileId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profileId\": ");

			sb.append("\"");

			sb.append(_escape(importProcess.getProfileId()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ImportProcessJSONParser importProcessJSONParser =
			new ImportProcessJSONParser();

		return importProcessJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ImportProcess importProcess) {
		if (importProcess == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (importProcess.getDebug() == null) {
			map.put("debug", null);
		}
		else {
			map.put("debug", String.valueOf(importProcess.getDebug()));
		}

		if (importProcess.getImporterNames() == null) {
			map.put("importerNames", null);
		}
		else {
			map.put(
				"importerNames",
				String.valueOf(importProcess.getImporterNames()));
		}

		if (importProcess.getProfileId() == null) {
			map.put("profileId", null);
		}
		else {
			map.put("profileId", String.valueOf(importProcess.getProfileId()));
		}

		return map;
	}

	public static class ImportProcessJSONParser
		extends BaseJSONParser<ImportProcess> {

		@Override
		protected ImportProcess createDTO() {
			return new ImportProcess();
		}

		@Override
		protected ImportProcess[] createDTOArray(int size) {
			return new ImportProcess[size];
		}

		@Override
		protected void setField(
			ImportProcess importProcess, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "debug")) {
				if (jsonParserFieldValue != null) {
					importProcess.setDebug((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "importerNames")) {
				if (jsonParserFieldValue != null) {
					importProcess.setImporterNames(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "profileId")) {
				if (jsonParserFieldValue != null) {
					importProcess.setProfileId((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
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
			sb.append("\":");

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
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}