package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.Report;
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
public class ReportSerDes {

	public static Report toDTO(String json) {
		ReportJSONParser reportJSONParser = new ReportJSONParser();

		return reportJSONParser.parseToDTO(json);
	}

	public static Report[] toDTOs(String json) {
		ReportJSONParser reportJSONParser = new ReportJSONParser();

		return reportJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Report report) {
		if (report == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (report.getContent() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"content\": ");

			sb.append("\"");

			sb.append(_escape(report.getContent()));

			sb.append("\"");
		}

		if (report.getIdentifier() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"identifier\": ");

			sb.append("\"");

			sb.append(_escape(report.getIdentifier()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ReportJSONParser reportJSONParser = new ReportJSONParser();

		return reportJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Report report) {
		if (report == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (report.getContent() == null) {
			map.put("content", null);
		}
		else {
			map.put("content", String.valueOf(report.getContent()));
		}

		if (report.getIdentifier() == null) {
			map.put("identifier", null);
		}
		else {
			map.put("identifier", String.valueOf(report.getIdentifier()));
		}

		return map;
	}

	public static class ReportJSONParser extends BaseJSONParser<Report> {

		@Override
		protected Report createDTO() {
			return new Report();
		}

		@Override
		protected Report[] createDTOArray(int size) {
			return new Report[size];
		}

		@Override
		protected void setField(
			Report report, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "content")) {
				if (jsonParserFieldValue != null) {
					report.setContent((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "identifier")) {
				if (jsonParserFieldValue != null) {
					report.setIdentifier((String)jsonParserFieldValue);
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