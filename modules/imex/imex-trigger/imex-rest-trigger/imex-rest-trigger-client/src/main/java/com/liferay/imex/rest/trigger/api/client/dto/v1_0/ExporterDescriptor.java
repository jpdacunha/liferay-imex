package com.liferay.imex.rest.trigger.api.client.dto.v1_0;

import com.liferay.imex.rest.trigger.api.client.function.UnsafeSupplier;
import com.liferay.imex.rest.trigger.api.client.serdes.v1_0.ExporterDescriptorSerDes;

import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author jpdacunha
 * @generated
 */
@Generated("")
public class ExporterDescriptor implements Cloneable {

	public static ExporterDescriptor toDTO(String json) {
		return ExporterDescriptorSerDes.toDTO(json);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescription(
		UnsafeSupplier<String, Exception> descriptionUnsafeSupplier) {

		try {
			description = descriptionUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String description;

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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setPriority(
		UnsafeSupplier<Integer, Exception> priorityUnsafeSupplier) {

		try {
			priority = priorityUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Integer priority;

	public Boolean getProfiled() {
		return profiled;
	}

	public void setProfiled(Boolean profiled) {
		this.profiled = profiled;
	}

	public void setProfiled(
		UnsafeSupplier<Boolean, Exception> profiledUnsafeSupplier) {

		try {
			profiled = profiledUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Boolean profiled;

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public void setRanking(
		UnsafeSupplier<String, Exception> rankingUnsafeSupplier) {

		try {
			ranking = rankingUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String ranking;

	public String[] getSupportedProfilesIds() {
		return supportedProfilesIds;
	}

	public void setSupportedProfilesIds(String[] supportedProfilesIds) {
		this.supportedProfilesIds = supportedProfilesIds;
	}

	public void setSupportedProfilesIds(
		UnsafeSupplier<String[], Exception>
			supportedProfilesIdsUnsafeSupplier) {

		try {
			supportedProfilesIds = supportedProfilesIdsUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] supportedProfilesIds;

	@Override
	public ExporterDescriptor clone() throws CloneNotSupportedException {
		return (ExporterDescriptor)super.clone();
	}

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
		return ExporterDescriptorSerDes.toJSON(this);
	}

}