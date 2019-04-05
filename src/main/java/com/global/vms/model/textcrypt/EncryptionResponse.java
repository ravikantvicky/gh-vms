package com.global.vms.model.textcrypt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EncryptionResponse {
	@JsonProperty("cipher_text")
	private String cipherText;

	public EncryptionResponse() {
		super();
	}

	public EncryptionResponse(String cipherText) {
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
