package com.liferay.imex.rest.trigger.api.client.dto.v1_0;

import com.liferay.imex.rest.trigger.api.client.function.UnsafeSupplier;
import com.liferay.imex.rest.trigger.api.client.serdes.v1_0.ImportProcessSerDes;

import java.io.Serializable;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class ImportProcess implements Cloneable, Serializable {

	public static ImportProcess toDTO(String json) {
		return ImportProcessSerDes.toDTO(json);
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

	public String[] getImporterNames() {
		return importerNames;
	}

	public void setImporterNames(String[] importerNames) {
		this.importerNames = importerNames;
	}

	public void setImporterNames(
		UnsafeSupplier<String[], Exception> importerNamesUnsafeSupplier) {

		try {
			importerNames = importerNamesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] importerNames;

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
	public ImportProcess clone() throws CloneNotSupportedException {
		return (ImportProcess)super.clone();
	}

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
		return ImportProcessSerDes.toJSON(this);
	}

}