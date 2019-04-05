package com.global.vms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PoiRequest {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Photo")
	private long photo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getPhoto() {
		return photo;
	}

	public void setPhoto(long photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "PoiRequest [name=" + name + ", description=" + description + ", type=" + type + ", photo=" + photo
				+ "]";
	}
}
