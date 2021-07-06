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

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
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
	description = "This is a representation for report logs files",
	value = "ReportFiles"
)
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "ReportFiles")
public class ReportFiles implements Serializable {

	public static ReportFiles toDTO(String json) {
		return ObjectMapperUtil.readValue(ReportFiles.class, json);
	}

	@Schema(description = "Date of creation")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonIgnore
	public void setCreationDate(
		UnsafeSupplier<Date, Exception> creationDateUnsafeSupplier) {

		try {
			creationDate = creationDateUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Date of creation")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Date creationDate;

	@Schema(description = "Size in octets")
	public String getHumanReadableSize() {
		return humanReadableSize;
	}

	public void setHumanReadableSize(String humanReadableSize) {
		this.humanReadableSize = humanReadableSize;
	}

	@JsonIgnore
	public void setHumanReadableSize(
		UnsafeSupplier<String, Exception> humanReadableSizeUnsafeSupplier) {

		try {
			humanReadableSize = humanReadableSizeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Size in octets")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String humanReadableSize;

	@Schema(description = "Date of last modification")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@JsonIgnore
	public void setLastModifiedDate(
		UnsafeSupplier<Date, Exception> lastModifiedDateUnsafeSupplier) {

		try {
			lastModifiedDate = lastModifiedDateUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Date of last modification")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Date lastModifiedDate;

	@Schema(description = "Name")
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

	@GraphQLField(description = "Name")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String name;

	@Schema(description = "Size in octets")
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@JsonIgnore
	public void setSize(UnsafeSupplier<Integer, Exception> sizeUnsafeSupplier) {
		try {
			size = sizeUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField(description = "Size in octets")
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer size;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof ReportFiles)) {
			return false;
		}

		ReportFiles reportFiles = (ReportFiles)object;

		return Objects.equals(toString(), reportFiles.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		if (creationDate != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"creationDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(creationDate));

			sb.append("\"");
		}

		if (humanReadableSize != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"humanReadableSize\": ");

			sb.append("\"");

			sb.append(_escape(humanReadableSize));

			sb.append("\"");
		}

		if (lastModifiedDate != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"lastModifiedDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(lastModifiedDate));

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

		if (size != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"size\": ");

			sb.append(size);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.imex.rest.trigger.api.dto.v1_0.ReportFiles",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
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
			sb.append(entry.getKey());
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
				sb.append(value);
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

}