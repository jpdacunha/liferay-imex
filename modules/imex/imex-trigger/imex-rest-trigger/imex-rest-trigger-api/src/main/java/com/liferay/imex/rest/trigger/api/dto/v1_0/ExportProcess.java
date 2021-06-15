package com.liferay.imex.rest.trigger.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
@GraphQLName("ExportProcess")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ExportProcess")
public class ExportProcess {

	public static ExportProcess toDTO(String json) {
		return ObjectMapperUtil.readValue(ExportProcess.class, json);
	}

	@Schema(
		description = "If true export process will be created in debug mode"
	)
	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	@JsonIgnore
	public void setDebug(
		UnsafeSupplier<Boolean, Exception> debugUnsafeSupplier) {

		try {
			debug = debugUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "If true export process will be created in debug mode"
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Boolean debug;

	@Schema(description = "List of restricted exporter names to trigger")
	public String[] getExporterNames() {
		return exporterNames;
	}

	public void setExporterNames(String[] exporterNames) {
		this.exporterNames = exporterNames;
	}

	@JsonIgnore
	public void setExporterNames(
		UnsafeSupplier<String[], Exception> exporterNamesUnsafeSupplier) {

		try {
			exporterNames = exporterNamesUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "List of restricted exporter names to trigger")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String[] exporterNames;

	@Schema(description = "Profile used to create new export process")
	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	@JsonIgnore
	public void setProfileId(
		UnsafeSupplier<String, Exception> profileIdUnsafeSupplier) {

		try {
			profileId = profileIdUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Profile used to create new export process")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String profileId;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ExportProcess)) {
			return false;
		}

		ExportProcess exportProcess = (ExportProcess)object;

		return Objects.equals(toString(), exportProcess.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (debug != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"debug\": ");

			sb.append(debug);
		}

		if (exporterNames != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"exporterNames\": ");

			sb.append("[");

			for (int i = 0; i < exporterNames.length; i++) {
				sb.append("\"");

				sb.append(_escape(exporterNames[i]));

				sb.append("\"");

				if ((i + 1) < exporterNames.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (profileId != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profileId\": ");

			sb.append("\"");

			sb.append(_escape(profileId));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		defaultValue = "com.liferay.imex.rest.trigger.api.dto.v1_0.ExportProcess",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
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

			Class<?> clazz = value.getClass();

			if (clazz.isArray()) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}