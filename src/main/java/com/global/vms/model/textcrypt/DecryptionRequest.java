package com.global.vms.model.textcrypt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecryptionRequest {
	@JsonProperty("cipher_text")
	private String cipherText;

	public DecryptionRequest() {
		super();
	}

	public DecryptionRequest(String cipherText) {
		super();
		this.cipherText = cipherText;
	}

	public String getCipherText() {
		return cipherText;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}
}
