package com.global.vms.model.sms;

import java.util.ArrayList;
import java.util.List;

public class SmsRequest {
	private String sender;
	private String route;
	private String country;
	private List<Message> sms = new ArrayList<>();

	public SmsRequest() {
	}

	public SmsRequest(String sender, String route, String country, List<Message> sms) {
		this.sender = sender;
		this.route = route;
		this.country = country;
		this.sms = sms;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<Message> getSms() {
		return sms;
	}

	public void setSms(List<Message> sms) {
		this.sms = sms;
	}
}
