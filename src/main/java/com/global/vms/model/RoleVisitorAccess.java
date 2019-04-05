package com.global.vms.model;

import com.global.vms.annotation.DbColumn;

public class RoleVisitorAccess {
	@DbColumn("visitor_type_cd")
	private String typeCode;
	@DbColumn("visitor_type_desc")
	private String typeDesc;

	public RoleVisitorAccess() {
	}

	public RoleVisitorAccess(String typeCode, String typeDesc) {
		this.typeCode = typeCode;
		this.typeDesc = typeDesc;
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
}
