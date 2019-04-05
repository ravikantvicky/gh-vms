package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class EmployeeFamily {
	@DbColumn
	private String name;
	@DbColumn
	private String email;
	@DbColumn
	private String mobile;
	@DbColumn
	private String relation;
	@DbColumn
	private long photoId;
	@DbColumn
	private String photo;

	public EmployeeFamily() {
	}

	public EmployeeFamily(String name, String email, String mobile, String relation, long photoId, String photo) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.relation = relation;
		this.photoId = photoId;
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
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
}
