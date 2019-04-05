package com.global.vms.model.sms;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private String message;
	private List<String> to = new ArrayList<>();

	public Message() {
	}

	public Message(String message, List<String> to) {
		this.message = message;
		this.to = to;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + ", to=" + to + "]";
	}
}
