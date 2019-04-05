package com.global.vms.model.textcrypt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptionRequest {
	@JsonProperty("plain_text")
	private String plainText;

	public EncryptionRequest() {
		super();
	}

	public EncryptionRequest(String plainText) {
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
