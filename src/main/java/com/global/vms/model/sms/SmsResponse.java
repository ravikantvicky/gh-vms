package com.global.vms.model.sms;

public class SmsResponse {
	private String message;
	private String type;

	public SmsResponse() {
	}

	public SmsResponse(String message, String type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
