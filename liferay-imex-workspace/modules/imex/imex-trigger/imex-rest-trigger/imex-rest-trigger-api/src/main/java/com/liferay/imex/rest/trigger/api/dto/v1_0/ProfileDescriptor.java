package com.liferay.imex.rest.trigger.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

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

import javax.validation.Valid;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
@GraphQLName(
	description = "This is the model representation for profile",
	value = "ProfileDescriptor"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ProfileDescriptor")
public class ProfileDescriptor implements Serializable {

	public static ProfileDescriptor toDTO(String json) {
		return ObjectMapperUtil.readValue(ProfileDescriptor.class, json);
	}

	public static ProfileDescriptor unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(ProfileDescriptor.class, json);
	}

	@Schema(description = "Criticity of datas associated with profile. ")
	@Valid
	public CriticityLevel getCriticityLevel() {
		return criticityLevel;
	}

	@JsonIgnore
	public String getCriticityLevelAsString() {
		if (criticityLevel == null) {
			return null;
		}

		return criticityLevel.toString();
	}

	public void setCriticityLevel(CriticityLevel criticityLevel) {
		this.criticityLevel = criticityLevel;
	}

	@JsonIgnore
	public void setCriticityLevel(
		UnsafeSupplier<CriticityLevel, Exception>
			criticityLevelUnsafeSupplier) {

		try {
			criticityLevel = criticityLevelUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Criticity of datas associated with profile. ")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected CriticityLevel criticityLevel;

	@Schema(description = "Profile name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Profile name")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String name;

	@Schema(description = "Profile unique identifier")
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

	@GraphQLField(description = "Profile unique identifier")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String profileId;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ProfileDescriptor)) {
			return false;
		}

		ProfileDescriptor profileDescriptor = (ProfileDescriptor)object;

		return Objects.equals(toString(), profileDescriptor.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (criticityLevel != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"criticityLevel\": ");

			sb.append("\"");

			sb.append(criticityLevel);

			sb.append("\"");
		}

		if (name != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(name));

			sb.append("\"");
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
		defaultValue = "com.liferay.imex.rest.trigger.api.dto.v1_0.ProfileDescriptor",
		name = "x-class-name"
	)
	public String xClassName;

	@GraphQLName("CriticityLevel")
	public static enum CriticityLevel {

		HIGH("high"), MEDIUM("medium"), NORMAL("normal"), LOW("low");

		@JsonCreator
		public static CriticityLevel create(String value) {
			if ((value == null) || value.equals("")) {
				return null;
			}

			for (CriticityLevel criticityLevel : values()) {
				if (Objects.equals(criticityLevel.getValue(), value)) {
					return criticityLevel;
				}
			}

			throw new IllegalArgumentException("Invalid enum value: " + value);
		}

		@JsonValue
		public String getValue() {
			return _value;
		}

		@Override
		public String toString() {
			return _value;
		}

		private CriticityLevel(String value) {
			_value = value;
		}

		private final String _value;

	}

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