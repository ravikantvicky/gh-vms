package com.global.vms.model.textcrypt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecryptionResponse {
	@JsonProperty("plain_text")
	private String plainText;

	public DecryptionResponse() {
		super();
	}

	public DecryptionResponse(String plainText) {
		super();
		this.plainText = plainText;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
}
