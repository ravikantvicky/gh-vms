package com.global.vms.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.global.vms.exception.VMSException;
import com.global.vms.helper.PdfHelper;
import com.global.vms.helper.SmsHelper;
import com.global.vms.model.LoginRequest;
import com.global.vms.model.LoginResponse;
import com.global.vms.model.ServiceResponse;
import com.global.vms.model.VisitorProfileRequest;
import com.global.vms.model.sms.Message;
import com.global.vms.service.ImageService;
import com.global.vms.service.VMSService;
import com.global.vms.util.AppConstants;
import com.global.vms.util.WebUtils;

@RestController
public class VMSController {

	private final Logger log = LoggerFactory.getLogger(VMSController.class);
	@Autowired
	private VMSService vmsService;
	@Autowired
	Environment env;
	@Autowired
	SmsHelper smsHelper;
	
	@Autowired
	PdfHelper pdfHelper;

	@Autowired
	ImageService imageService;

	@PostMapping("/login")
	public ServiceResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request)
			throws VMSException {
		try {
			return new ServiceResponse<>(env.getProperty("status.success", Integer.class), null,
					vmsService.login(loginRequest, WebUtils.getIp(request)));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in login: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}

	@GetMapping("/approve/{data}")
	public void approve(@PathVariable("data") String cryptData, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html");

		try {
			vmsService.approve(cryptData);
			response.getWriter().write(
					"<h3 style='text-align:center;color:green'>" + env.getProperty("message.approved") + "</h3>");
		} catch (VMSException e) {
			try {
				response.getWriter().write("<h3 style='text-align:center;color:red'>" + e.getMessage() + "</h3>");
			} catch (IOException e1) {
				log.error("Error in approval", e1);
			}
		} catch (Exception e) {
			log.error("Error in approval", e);
			try {
				response.getWriter().write(
						"<h3 style='text-align:center;color:red'>" + env.getProperty("errormsg.approval") + "</h3>");
			} catch (IOException e1) {
				log.error("Error in approval", e1);
			}
		}
	}

	@GetMapping(value = "/")
	public String generatePDF(HttpServletResponse response) throws VMSException {
		String msgBody = env.getProperty("sms.alert.message").replace(AppConstants.MSG_PLACEHOLDER_NAME, "Ravi")
				.replace(AppConstants.MSG_PLACEHOLDER_EXIT_TIME, "12:30 PM");
		List<Message> msgs = new ArrayList<>();
		msgs.add(new Message(msgBody, Arrays.asList(new String[] { "8917372288" })));
		// smsHelper.sendSms(msgs);
		return "Application up and running !";
	}

	@GetMapping("/images/{imageId}")
	public void viewImage(@PathVariable("imageId") long imageId, HttpServletResponse response) {
		try {
			byte[] imageData = imageService.getImage(imageId);
			InputStream is = new ByteArrayInputStream(imageData);
			String mimeType = URLConnection.guessContentTypeFromStream(is);
			response.setContentType(mimeType);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(imageData);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/gatePass/{visitorId}")
	public void getGatePass(@PathVariable("visitorId") long imageId, HttpServletResponse response) {
		try {
			byte[] pdfData = pdfHelper.generateGatePass(vmsService.fetchVisitorProfile(new VisitorProfileRequest(String.valueOf(imageId), "", 0)));
			InputStream is = new ByteArrayInputStream(pdfData);
			String mimeType = URLConnection.guessContentTypeFromStream(is);
			response.setContentType(mimeType);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(pdfData);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
