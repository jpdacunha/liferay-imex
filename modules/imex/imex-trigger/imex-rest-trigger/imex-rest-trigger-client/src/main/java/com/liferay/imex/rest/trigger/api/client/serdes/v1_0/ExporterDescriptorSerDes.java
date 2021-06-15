package com.liferay.imex.rest.trigger.api.client.serdes.v1_0;

import com.liferay.imex.rest.trigger.api.client.dto.v1_0.ExporterDescriptor;
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
public class ExporterDescriptorSerDes {

	public static ExporterDescriptor toDTO(String json) {
		ExporterDescriptorJSONParser exporterDescriptorJSONParser =
			new ExporterDescriptorJSONParser();

		return exporterDescriptorJSONParser.parseToDTO(json);
	}

	public static ExporterDescriptor[] toDTOs(String json) {
		ExporterDescriptorJSONParser exporterDescriptorJSONParser =
			new ExporterDescriptorJSONParser();

		return exporterDescriptorJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ExporterDescriptor exporterDescriptor) {
		if (exporterDescriptor == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (exporterDescriptor.getDescription() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"description\": ");

			sb.append("\"");

			sb.append(_escape(exporterDescriptor.getDescription()));

			sb.append("\"");
		}

		if (exporterDescriptor.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(exporterDescriptor.getName()));

			sb.append("\"");
		}

		if (exporterDescriptor.getPriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append(exporterDescriptor.getPriority());
		}

		if (exporterDescriptor.getProfiled() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profiled\": ");

			sb.append(exporterDescriptor.getProfiled());
		}

		if (exporterDescriptor.getRanking() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ranking\": ");

			sb.append("\"");

			sb.append(_escape(exporterDescriptor.getRanking()));

			sb.append("\"");
		}

		if (exporterDescriptor.getSupportedProfilesIds() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"supportedProfilesIds\": ");

			sb.append("[");

			for (int i = 0;
				 i < exporterDescriptor.getSupportedProfilesIds().length; i++) {

				sb.append("\"");

				sb.append(
					_escape(exporterDescriptor.getSupportedProfilesIds()[i]));

				sb.append("\"");

				if ((i + 1) <
						exporterDescriptor.getSupportedProfilesIds().length) {

					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ExporterDescriptorJSONParser exporterDescriptorJSONParser =
			new ExporterDescriptorJSONParser();

		return exporterDescriptorJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ExporterDescriptor exporterDescriptor) {

		if (exporterDescriptor == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (exporterDescriptor.getDescription() == null) {
			map.put("description", null);
		}
		else {
			map.put(
				"description",
				String.valueOf(exporterDescriptor.getDescription()));
		}

		if (exporterDescriptor.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(exporterDescriptor.getName()));
		}

		if (exporterDescriptor.getPriority() == null) {
			map.put("priority", null);
		}
		else {
			map.put(
				"priority", String.valueOf(exporterDescriptor.getPriority()));
		}

		if (exporterDescriptor.getProfiled() == null) {
			map.put("profiled", null);
		}
		else {
			map.put(
				"profiled", String.valueOf(exporterDescriptor.getProfiled()));
		}

		if (exporterDescriptor.getRanking() == null) {
			map.put("ranking", null);
		}
		else {
			map.put("ranking", String.valueOf(exporterDescriptor.getRanking()));
		}

		if (exporterDescriptor.getSupportedProfilesIds() == null) {
			map.put("supportedProfilesIds", null);
		}
		else {
			map.put(
				"supportedProfilesIds",
				String.valueOf(exporterDescriptor.getSupportedProfilesIds()));
		}

		return map;
	}

	public static class ExporterDescriptorJSONParser
		extends BaseJSONParser<ExporterDescriptor> {

		@Override
		protected ExporterDescriptor createDTO() {
			return new ExporterDescriptor();
		}

		@Override
		protected ExporterDescriptor[] createDTOArray(int size) {
			return new ExporterDescriptor[size];
		}

		@Override
		protected void setField(
			ExporterDescriptor exporterDescriptor, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					exporterDescriptor.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					exporterDescriptor.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "priority")) {
				if (jsonParserFieldValue != null) {
					exporterDescriptor.setPriority(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "profiled")) {
				if (jsonParserFieldValue != null) {
					exporterDescriptor.setProfiled(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "ranking")) {
				if (jsonParserFieldValue != null) {
					exporterDescriptor.setRanking((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "supportedProfilesIds")) {

				if (jsonParserFieldValue != null) {
					exporterDescriptor.setSupportedProfilesIds(
						toStrings((Object[])jsonParserFieldValue));
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