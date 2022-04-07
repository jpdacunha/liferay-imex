package com.liferay.imex.rest.trigger.api.client.dto.v1_0;

import com.liferay.imex.rest.trigger.api.client.function.UnsafeSupplier;
import com.liferay.imex.rest.trigger.api.client.serdes.v1_0.ExportProcessSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class ExportProcess implements Cloneable, Serializable {

	public static ExportProcess toDTO(String json) {
		return ExportProcessSerDes.toDTO(json);
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public void setDebug(
		UnsafeSupplier<Boolean, Exception> debugUnsafeSupplier) {

		try {
			debug = debugUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean debug;

	public String[] getExporterNames() {
		return exporterNames;
	}

	public void setExporterNames(String[] exporterNames) {
		this.exporterNames = exporterNames;
	}

	public void setExporterNames(
		UnsafeSupplier<String[], Exception> exporterNamesUnsafeSupplier) {

		try {
			exporterNames = exporterNamesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] exporterNames;

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public void setProfileId(
		UnsafeSupplier<String, Exception> profileIdUnsafeSupplier) {

		try {
			profileId = profileIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String profileId;

	@Override
	public ExportProcess clone() throws CloneNotSupportedException {
		return (ExportProcess)super.clone();
	}

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
		return ExportProcessSerDes.toJSON(this);
	}

}