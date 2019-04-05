package com.global.vms.model;

public class VisitorRegistrationResponse {
	private int status;
	private String message;
	private VisitorProfileResponse data;

	public VisitorRegistrationResponse() {
		super();
	}

	public VisitorRegistrationResponse(int status, String message, VisitorProfileResponse data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public VisitorProfileResponse getData() {
		return data;
	}

	public void setData(VisitorProfileResponse data) {
		this.data = data;
	}
}
