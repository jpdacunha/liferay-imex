package com.liferay.imex.rest.trigger.api.client.dto.v1_0;

import com.liferay.imex.rest.trigger.api.client.function.UnsafeSupplier;
import com.liferay.imex.rest.trigger.api.client.serdes.v1_0.ReportFilesSerDes;

import java.io.Serializable;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class ReportFiles implements Cloneable, Serializable {

	public static ReportFiles toDTO(String json) {
		return ReportFilesSerDes.toDTO(json);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setCreationDate(
		UnsafeSupplier<Date, Exception> creationDateUnsafeSupplier) {

		try {
			creationDate = creationDateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date creationDate;

	public String getHumanReadableSize() {
		return humanReadableSize;
	}

	public void setHumanReadableSize(String humanReadableSize) {
		this.humanReadableSize = humanReadableSize;
	}

	public void setHumanReadableSize(
		UnsafeSupplier<String, Exception> humanReadableSizeUnsafeSupplier) {

		try {
			humanReadableSize = humanReadableSizeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String humanReadableSize;

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setLastModifiedDate(
		UnsafeSupplier<Date, Exception> lastModifiedDateUnsafeSupplier) {

		try {
			lastModifiedDate = lastModifiedDateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date lastModifiedDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setSize(UnsafeSupplier<Integer, Exception> sizeUnsafeSupplier) {
		try {
			size = sizeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer size;

	@Override
	public ReportFiles clone() throws CloneNotSupportedException {
		return (ReportFiles)super.clone();
	}

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
		return ReportFilesSerDes.toJSON(this);
	}

}