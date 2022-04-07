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
	description = "This is the model representation for exporter",
	value = "ExporterDescriptor"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ExporterDescriptor")
public class ExporterDescriptor implements Serializable {

	public static ExporterDescriptor toDTO(String json) {
		return ObjectMapperUtil.readValue(ExporterDescriptor.class, json);
	}

	public static ExporterDescriptor unsafeToDTO(String json) {
		return ObjectMapperUtil.readValue(ExporterDescriptor.class, json);
	}

	@Schema(description = "Exporter description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public void setDescription(
		UnsafeSupplier<String, Exception> descriptionUnsafeSupplier) {

		try {
			description = descriptionUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Exporter description")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String description;

	@Schema(description = "Unique exporter name")
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

	@GraphQLField(description = "Unique exporter name")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String name;

	@Schema(description = "Exporter execution priority")
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@JsonIgnore
	public void setPriority(
		UnsafeSupplier<Integer, Exception> priorityUnsafeSupplier) {

		try {
			priority = priorityUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Exporter execution priority")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer priority;

	@Schema(
		description = "Setted to true if exporter is abble to work in profiled mode"
	)
	public Boolean getProfiled() {
		return profiled;
	}

	public void setProfiled(Boolean profiled) {
		this.profiled = profiled;
	}

	@JsonIgnore
	public void setProfiled(
		UnsafeSupplier<Boolean, Exception> profiledUnsafeSupplier) {

		try {
			profiled = profiledUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(
		description = "Setted to true if exporter is abble to work in profiled mode"
	)
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Boolean profiled;

	@Schema(description = "Exporter OSGI ranking")
	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	@JsonIgnore
	public void setRanking(
		UnsafeSupplier<String, Exception> rankingUnsafeSupplier) {

		try {
			ranking = rankingUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Exporter OSGI ranking")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String ranking;

	@Schema(description = "Profiles supported by exporter")
	public String[] getSupportedProfilesIds() {
		return supportedProfilesIds;
	}

	public void setSupportedProfilesIds(String[] supportedProfilesIds) {
		this.supportedProfilesIds = supportedProfilesIds;
	}

	@JsonIgnore
	public void setSupportedProfilesIds(
		UnsafeSupplier<String[], Exception>
			supportedProfilesIdsUnsafeSupplier) {

		try {
			supportedProfilesIds = supportedProfilesIdsUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Profiles supported by exporter")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String[] supportedProfilesIds;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ExporterDescriptor)) {
			return false;
		}

		ExporterDescriptor exporterDescriptor = (ExporterDescriptor)object;

		return Objects.equals(toString(), exporterDescriptor.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (description != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"description\": ");

			sb.append("\"");

			sb.append(_escape(description));

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

		if (priority != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append(priority);
		}

		if (profiled != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"profiled\": ");

			sb.append(profiled);
		}

		if (ranking != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"ranking\": ");

			sb.append("\"");

			sb.append(_escape(ranking));

			sb.append("\"");
		}

		if (supportedProfilesIds != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"supportedProfilesIds\": ");

			sb.append("[");

			for (int i = 0; i < supportedProfilesIds.length; i++) {
				sb.append("\"");

				sb.append(_escape(supportedProfilesIds[i]));

				sb.append("\"");

				if ((i + 1) < supportedProfilesIds.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.imex.rest.trigger.api.dto.v1_0.ExporterDescriptor",
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