package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class ApprovedVisitorsToday {
	@DbColumn("visitor_id")
	private long visitorId;
	@DbColumn("name")
	private String visitorName;
	@DbColumn("visitor_type")
	private String typeCode;
	@DbColumn("visitor_type_desc")
	private String typeDesc;
	@DbColumn("referred_by")
	private String referredBy;

	public ApprovedVisitorsToday() {
	}

	public ApprovedVisitorsToday(long visitorId, String visitorName, String typeCode, String typeDesc, String referredBy) {
		this.visitorId = visitorId;
		this.visitorName = visitorName;
		this.typeCode = typeCode;
		this.typeDesc = typeDesc;
		this.referredBy = referredBy;
	}

	public long getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(long visitorId) {
		this.visitorId = visitorId;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}
}
