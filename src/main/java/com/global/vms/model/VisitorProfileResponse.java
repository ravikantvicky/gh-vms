package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class VisitorProfileResponse {
	@DbColumn("visitor_id")
	private long visitorId;
	@DbColumn
	private String name;
	@DbColumn("visitor_type")
	private String visitorType;
	@DbColumn
	private String email;
	@DbColumn
	private String mobile;
	@DbColumn
	private String photo;
	@DbColumn("referred_by")
	private String referredBy;
	@DbColumn("expected_in_time")
	private String expectedEntry;
	@DbColumn("actual_in_time")
	private String actualEntry;
	@DbColumn("expected_out_time")
	private String expectedExit;
	@DbColumn("actual_out_time")
	private String actualExit;
	@DbColumn("status")
	private int visitorStatus;
	@DbColumn("is_spot_registration")
	private int spotRegistration;
	private String qrCode;

	public long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(long visitorId) {
		this.visitorId = visitorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisitorType() {
		return visitorType;
	}

	public void setVisitorType(String visitorType) {
		this.visitorType = visitorType;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}

	public String getExpectedEntry() {
		return expectedEntry;
	}

	public void setExpectedEntry(String expectedEntry) {
		this.expectedEntry = expectedEntry;
	}

	public String getActualEntry() {
		return actualEntry;
	}

	public void setActualEntry(String actualEntry) {
		this.actualEntry = actualEntry;
	}

	public String getExpectedExit() {
		return expectedExit;
	}

	public void setExpectedExit(String expectedExit) {
		this.expectedExit = expectedExit;
	}

	public String getActualExit() {
		return actualExit;
	}

	public void setActualExit(String actualExit) {
		this.actualExit = actualExit;
	}

	public int getVisitorStatus() {
		return visitorStatus;
	}

	public void setVisitorStatus(int visitorStatus) {
		this.visitorStatus = visitorStatus;
	}

	public int getSpotRegistration() {
		return spotRegistration;
	}

	public void setSpotRegistration(int spotRegistration) {
		this.spotRegistration = spotRegistration;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
}
