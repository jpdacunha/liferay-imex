package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ExportProcess;
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
public class ExportProcessSerDes {

	public static ExportProcess toDTO(String json) {
		ExportProcessJSONParser exportProcessJSONParser =
			new ExportProcessJSONParser();

		return exportProcessJSONParser.parseToDTO(json);
	}

	public static ExportProcess[] toDTOs(String json) {
		ExportProcessJSONParser exportProcessJSONParser =
			new ExportProcessJSONParser();

		return exportProcessJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ExportProcess exportProcess) {
		if (exportProcess == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (exportProcess.getDebug() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"debug\": ");

			sb.append(exportProcess.getDebug());
		}

		if (exportProcess.getExporterNames() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"exporterNames\": ");

			sb.append("[");

			for (int i = 0; i < exportProcess.getExporterNames().length; i++) {
				sb.append("\"");

				sb.append(_escape(exportProcess.getExporterNames()[i]));

				sb.append("\"");

				if ((i + 1) < exportProcess.getExporterNames().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (exportProcess.getProfileId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profileId\": ");

			sb.append("\"");

			sb.append(_escape(exportProcess.getProfileId()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ExportProcessJSONParser exportProcessJSONParser =
			new ExportProcessJSONParser();

		return exportProcessJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ExportProcess exportProcess) {
		if (exportProcess == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (exportProcess.getDebug() == null) {
			map.put("debug", null);
		}
		else {
			map.put("debug", String.valueOf(exportProcess.getDebug()));
		}

		if (exportProcess.getExporterNames() == null) {
			map.put("exporterNames", null);
		}
		else {
			map.put(
				"exporterNames",
				String.valueOf(exportProcess.getExporterNames()));
		}

		if (exportProcess.getProfileId() == null) {
			map.put("profileId", null);
		}
		else {
			map.put("profileId", String.valueOf(exportProcess.getProfileId()));
		}

		return map;
	}

	public static class ExportProcessJSONParser
		extends BaseJSONParser<ExportProcess> {

		@Override
		protected ExportProcess createDTO() {
			return new ExportProcess();
		}

		@Override
		protected ExportProcess[] createDTOArray(int size) {
			return new ExportProcess[size];
		}

		@Override
		protected void setField(
			ExportProcess exportProcess, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "debug")) {
				if (jsonParserFieldValue != null) {
					exportProcess.setDebug((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "exporterNames")) {
				if (jsonParserFieldValue != null) {
					exportProcess.setExporterNames(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "profileId")) {
				if (jsonParserFieldValue != null) {
					exportProcess.setProfileId((String)jsonParserFieldValue);
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