package com.liferay.imex.rest.trigger.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

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
@GraphQLName(
	description = "This is the model for creating a new import process",
	value = "ImportProcess"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ImportProcess")
public class ImportProcess implements Serializable {

	public static ImportProcess toDTO(String json) {
		return ObjectMapperUtil.readValue(ImportProcess.class, json);
	}

	public static ImportProcess unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(ImportProcess.class, json);
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

	@Schema(description = "List of restricted importer names to trigger")
	public String[] getImporterNames() {
		return importerNames;
	}

	public void setImporterNames(String[] importerNames) {
		this.importerNames = importerNames;
	}

	@JsonIgnore
	public void setImporterNames(
		UnsafeSupplier<String[], Exception> importerNamesUnsafeSupplier) {

		try {
			importerNames = importerNamesUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "List of restricted importer names to trigger")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String[] importerNames;

	@Schema(description = "Profile used to create new import process")
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

	@GraphQLField(description = "Profile used to create new import process")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String profileId;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ImportProcess)) {
			return false;
		}

		ImportProcess importProcess = (ImportProcess)object;

		return Objects.equals(toString(), importProcess.toString());
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

		if (importerNames != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"importerNames\": ");

			sb.append("[");

			for (int i = 0; i < importerNames.length; i++) {
				sb.append("\"");

				sb.append(_escape(importerNames[i]));

				sb.append("\"");

				if ((i + 1) < importerNames.length) {
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
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.imex.rest.trigger.api.dto.v1_0.ImportProcess",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
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
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
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
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

}