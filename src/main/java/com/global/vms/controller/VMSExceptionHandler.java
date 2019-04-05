package com.global.vms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.global.vms.exception.VMSException;
import com.global.vms.model.ServiceResponse;

@ControllerAdvice
@RestController
public class VMSExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	Environment env;

	@ExceptionHandler(VMSException.class)
	public final ResponseEntity<ServiceResponse<Object>> commonExceptionHandler(VMSException ex,
			WebRequest webRequest) {
		ServiceResponse<Object> respone = new ServiceResponse<>(env.getProperty("status.error", Integer.class),
				ex.getMessage(), null);
		return new ResponseEntity<ServiceResponse<Object>>(respone, HttpStatus.OK);
	}
}
