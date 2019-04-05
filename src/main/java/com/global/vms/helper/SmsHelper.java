package com.global.vms.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.global.vms.exception.VMSException;
import com.global.vms.model.sms.Message;
import com.global.vms.model.sms.SmsRequest;
import com.global.vms.model.sms.SmsResponse;

@Service
public class SmsHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Environment env;

	public void sendSms(List<Message> messages) throws VMSException {
		log.info("Sending SMS: {}", messages);
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = env.getProperty("sms.api.url");
			SmsRequest request = new SmsRequest();
			request.setCountry(env.getProperty("sms.api.country"));
			request.setRoute(env.getProperty("sms.api.route"));
			request.setSender(env.getProperty("sms.api.sender"));
			request.setSms(messages);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			headers.add(env.getProperty("sms.api.authkey.key"), env.getProperty("sms.api.authkey.value"));

			ResponseEntity<SmsResponse> response = restTemplate.exchange(url, HttpMethod.POST,
					new HttpEntity<SmsRequest>(request, headers), SmsResponse.class);
			if (response.getStatusCode() != HttpStatus.OK) {
				log.error("Send SMS unsuccessfull, recieved Http Status: {}", response.getStatusCode().toString());
				throw new VMSException(env.getProperty("errormsg.sendsms"));
			}
			if (response.getBody() == null) {
				log.error("Recieved empty body while sending sms.");
				throw new VMSException(env.getProperty("errormsg.sendsms"));
			} else if (!response.getBody().getType().equalsIgnoreCase(env.getProperty("sms.api.status.success"))) {
				log.error("Response from sms api: {}", response.getBody().getType());
				throw new VMSException(env.getProperty("errormsg.sendsms"));
			}
			log.info("SMS sent, status code: {}", response.getBody().getType());
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in sending sms", e);
			throw new VMSException(env.getProperty("errormsg.sendsms"));
		}
	}
}
