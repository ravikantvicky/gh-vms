package com.global.vms.model;

public class LocationAccessRequest {
	private String visitorId;
	private int encrypted;
	private long securityId;

	public String getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(String visitorId) {
		this.visitorId = visitorId;
	}

	public int getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(int encrypted) {
		this.encrypted = encrypted;
	}

	public long getSecurityId() {
		return securityId;
	}

	public void setSecurityId(long securityId) {
		this.securityId = securityId;
	}
}
