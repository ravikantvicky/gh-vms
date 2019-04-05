package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class PersonOfInterest {
	@DbColumn("id")
	private long personId;
	@DbColumn
	private String name;
	@DbColumn
	private String type;
	@DbColumn("image_id")
	private long photoId;
	@DbColumn("image_data")
	private String photo;
	@DbColumn("description")
	private String description;
	@DbColumn("created_at")
	private String createdAt;
	@DbColumn("updated_at")
	private String modifiedAt;

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "PersonOfInterest [personId=" + personId + ", name=" + name + ", type=" + type + ", photoId=" + photoId
				+ ", photo=" + photo + ", description=" + description + ", createdAt=" + createdAt + ", modifiedAt="
				+ modifiedAt + "]";
	}
}
