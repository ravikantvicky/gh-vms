package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class Employee {
	@DbColumn("id")
	private long empId;
	@DbColumn
	private String name;
	@DbColumn
	private String email;
	@DbColumn
	private String mobile;
	@DbColumn
	private long photoId;
	@DbColumn
	private String photo;

	public Employee() {
		super();
	}

	public Employee(long empId, String name, String email, String mobile, long photoId, String photo) {
		super();
		this.empId = empId;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.photoId = photoId;
		this.photo = photo;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
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
