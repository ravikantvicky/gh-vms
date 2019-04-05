package com.global.vms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.global.vms.exception.VMSException;
import com.global.vms.model.textcrypt.EncryptionResponse;
import com.global.vms.model.textcrypt.DecryptionRequest;
import com.global.vms.model.textcrypt.DecryptionResponse;
import com.global.vms.model.textcrypt.EncryptionRequest;

@Service
public class TextCryptService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	Environment env;

	public String encryptText(String plainText) throws VMSException {
		log.info("method encryptText started");
		try {
			String url = env.getProperty("service.url.ml-crypt-baseUrl") + env.getProperty("service.path.encrypt");
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", "application/json");
			EncryptionRequest requestBody = new EncryptionRequest(plainText);
			ResponseEntity<EncryptionResponse> respEnt = restTemplate.exchange(url, HttpMethod.POST,
					new HttpEntity<EncryptionRequest>(requestBody, header), EncryptionResponse.class);
			log.info("method encryptText completed");
			return respEnt.getBody().getCipherText();
		} catch (Exception e) {
			log.error("Error in encryptText:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	public String decryptText(String cipher) throws VMSException {
		log.info("method decryptText started");
		try {
			String url = env.getProperty("service.url.ml-crypt-baseUrl") + env.getProperty("service.path.decrypt");
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", "application/json");
			DecryptionRequest requestBody = new DecryptionRequest(cipher);
			ResponseEntity<DecryptionResponse> respEnt = restTemplate.exchange(url, HttpMethod.POST,
					new HttpEntity<DecryptionRequest>(requestBody, header), DecryptionResponse.class);
			log.info("method decryptText completed");
			return respEnt.getBody().getPlainText();
		} catch (Exception e) {
			log.error("Error in decryptText:", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
