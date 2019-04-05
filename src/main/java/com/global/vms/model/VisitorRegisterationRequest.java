package com.global.vms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitorRegisterationRequest {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Mobile")
	private String mobile;
	@JsonProperty("Photo")
	private long photo;
	@JsonProperty("Reffered")
	private long refferedBy;
	@JsonProperty("VisitorType")
	private String visitorType;
	@JsonProperty("IN")
	private String inTime;
	@JsonProperty("OUT")
	private String outTime;

	public VisitorRegisterationRequest() {
	}

	public VisitorRegisterationRequest(String name, String email, String mobile, long photo, long refferedBy,
			String visitorType, String inTime, String outTime) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.photo = photo;
		this.refferedBy = refferedBy;
		this.visitorType = visitorType;
		this.inTime = inTime;
		this.outTime = outTime;
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

	public long getPhoto() {
		return photo;
	}

	public void setPhoto(long photo) {
		this.photo = photo;
	}

	public long getRefferedBy() {
		return refferedBy;
	}

	public void setRefferedBy(long refferedBy) {
		this.refferedBy = refferedBy;
	}

	public String getVisitorType() {
		return visitorType;
	}

	public void setVisitorType(String visitorType) {
		this.visitorType = visitorType;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
}
