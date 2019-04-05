package com.global.vms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.global.vms.exception.VMSException;

@Component
public class MailContentService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Environment env;

	public String getMailBody(String url) throws VMSException {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String mailBody = restTemplate.getForObject(url, String.class);
			return mailBody;
		} catch (Exception e) {
			log.error("Error in fetching mail body:", e);
			throw new VMSException(env.getProperty("errormsg.sendmail"));
		}
	}

	public byte[] getMailAttachment(String url) throws VMSException {
		try {
			RestTemplate restTemplate = new RestTemplate();
			byte[] mailBody = restTemplate.getForObject(url, byte[].class);
			return mailBody;
		} catch (Exception e) {
			log.error("Error in fetching mail body:", e);
			throw new VMSException(env.getProperty("errormsg.sendmail"));
		}
	}
}
