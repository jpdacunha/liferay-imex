package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ReportFiles;
import com.liferay.imex.rest.trigger.api.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
public class ReportFilesSerDes {

	public static ReportFiles toDTO(String json) {
		ReportFilesJSONParser reportFilesJSONParser =
			new ReportFilesJSONParser();

		return reportFilesJSONParser.parseToDTO(json);
	}

	public static ReportFiles[] toDTOs(String json) {
		ReportFilesJSONParser reportFilesJSONParser =
			new ReportFilesJSONParser();

		return reportFilesJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ReportFiles reportFiles) {
		if (reportFiles == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (reportFiles.getCreationDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"creationDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(reportFiles.getCreationDate()));

			sb.append("\"");
		}

		if (reportFiles.getHumanReadableSize() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"humanReadableSize\": ");

			sb.append("\"");

			sb.append(_escape(reportFiles.getHumanReadableSize()));

			sb.append("\"");
		}

		if (reportFiles.getLastModifiedDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"lastModifiedDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					reportFiles.getLastModifiedDate()));

			sb.append("\"");
		}

		if (reportFiles.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(reportFiles.getName()));

			sb.append("\"");
		}

		if (reportFiles.getSize() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"size\": ");

			sb.append(reportFiles.getSize());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ReportFilesJSONParser reportFilesJSONParser =
			new ReportFilesJSONParser();

		return reportFilesJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ReportFiles reportFiles) {
		if (reportFiles == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (reportFiles.getCreationDate() == null) {
			map.put("creationDate", null);
		}
		else {
			map.put(
				"creationDate",
				liferayToJSONDateFormat.format(reportFiles.getCreationDate()));
		}

		if (reportFiles.getHumanReadableSize() == null) {
			map.put("humanReadableSize", null);
		}
		else {
			map.put(
				"humanReadableSize",
				String.valueOf(reportFiles.getHumanReadableSize()));
		}

		if (reportFiles.getLastModifiedDate() == null) {
			map.put("lastModifiedDate", null);
		}
		else {
			map.put(
				"lastModifiedDate",
				liferayToJSONDateFormat.format(
					reportFiles.getLastModifiedDate()));
		}

		if (reportFiles.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(reportFiles.getName()));
		}

		if (reportFiles.getSize() == null) {
			map.put("size", null);
		}
		else {
			map.put("size", String.valueOf(reportFiles.getSize()));
		}

		return map;
	}

	public static class ReportFilesJSONParser
		extends BaseJSONParser<ReportFiles> {

		@Override
		protected ReportFiles createDTO() {
			return new ReportFiles();
		}

		@Override
		protected ReportFiles[] createDTOArray(int size) {
			return new ReportFiles[size];
		}

		@Override
		protected void setField(
			ReportFiles reportFiles, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "creationDate")) {
				if (jsonParserFieldValue != null) {
					reportFiles.setCreationDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "humanReadableSize")) {
				if (jsonParserFieldValue != null) {
					reportFiles.setHumanReadableSize(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "lastModifiedDate")) {
				if (jsonParserFieldValue != null) {
					reportFiles.setLastModifiedDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					reportFiles.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "size")) {
				if (jsonParserFieldValue != null) {
					reportFiles.setSize(
						Integer.valueOf((String)jsonParserFieldValue));
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