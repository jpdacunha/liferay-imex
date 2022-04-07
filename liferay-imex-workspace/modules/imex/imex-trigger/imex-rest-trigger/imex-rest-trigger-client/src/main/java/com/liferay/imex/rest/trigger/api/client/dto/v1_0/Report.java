package com.liferay.imex.rest.trigger.api.client.dto.v1_0;

import com.liferay.imex.rest.trigger.api.client.function.UnsafeSupplier;
import com.liferay.imex.rest.trigger.api.client.serdes.v1_0.ReportSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class Report implements Cloneable, Serializable {

	public static Report toDTO(String json) {
		return ReportSerDes.toDTO(json);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContent(
		UnsafeSupplier<String, Exception> contentUnsafeSupplier) {

		try {
			content = contentUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String content;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setIdentifier(
		UnsafeSupplier<String, Exception> identifierUnsafeSupplier) {

		try {
			identifier = identifierUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String identifier;

	@Override
	public Report clone() throws CloneNotSupportedException {
		return (Report)super.clone();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof Report)) {
			return false;
		}

		Report report = (Report)object;

		return Objects.equals(toString(), report.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return ReportSerDes.toJSON(this);
	}

}