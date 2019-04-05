package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class VisitorExceedOutTime {
	@DbColumn("id")
	private long visitorId;
	@DbColumn
	private String name;
	@DbColumn
	private String mobile;
	@DbColumn("expected_out_time")
	private String outTime;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
}
