package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class InsideVisitor {
	@DbColumn("visitor_id")
	private long visitorId;
	@DbColumn("visitor_type")
	private String visitorType;
	@DbColumn
	private String name;
	@DbColumn
	private String email;
	@DbColumn
	private String mobile;
	@DbColumn("photo_id")
	private long photoId;
	@DbColumn("photo_url")
	private String photoUrl;
	@DbColumn("actual_in_time")
	private String actualInTime;
	@DbColumn("expected_out_time")
	private String expectedOutTime;
	@DbColumn
	private int status;
	@DbColumn("ref_name")
	private String refName;
	@DbColumn("ref_mobile")
	private String refMobile;
	@DbColumn("time_exceed")
	private String timeExceed;

	public InsideVisitor() {
	}

	public InsideVisitor(long visitorId, String visitorType, String name, String email, String mobile, long photoId,
			String photoUrl, String actualInTime, String expectedOutTime, int status, String refName, String refMobile,
			String timeExceed) {
		this.visitorId = visitorId;
		this.visitorType = visitorType;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.photoId = photoId;
		this.photoUrl = photoUrl;
		this.actualInTime = actualInTime;
		this.expectedOutTime = expectedOutTime;
		this.status = status;
		this.refName = refName;
		this.refMobile = refMobile;
		this.timeExceed = timeExceed;
	}

	public long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(long visitorId) {
		this.visitorId = visitorId;
	}

	public String getVisitorType() {
		return visitorType;
	}

	public void setVisitorType(String visitorType) {
		this.visitorType = visitorType;
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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getActualInTime() {
		return actualInTime;
	}

	public void setActualInTime(String actualInTime) {
		this.actualInTime = actualInTime;
	}

	public String getExpectedOutTime() {
		return expectedOutTime;
	}

	public void setExpectedOutTime(String expectedOutTime) {
		this.expectedOutTime = expectedOutTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getRefMobile() {
		return refMobile;
	}

	public void setRefMobile(String refMobile) {
		this.refMobile = refMobile;
	}

	public String getTimeExceed() {
		return timeExceed;
	}

	public void setTimeExceed(String timeExceed) {
		this.timeExceed = timeExceed;
	}
}
