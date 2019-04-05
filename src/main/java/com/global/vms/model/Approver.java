package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class Approver {
	@DbColumn
	private long id;
	@DbColumn
	private String name;
	@DbColumn
	private String email;
	@DbColumn
	private String mobile;

	public Approver() {
	}

	public Approver(long id, String name, String email, String mobile) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
}
