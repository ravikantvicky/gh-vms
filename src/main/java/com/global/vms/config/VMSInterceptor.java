package com.global.vms.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.vms.helper.TokenUtils;
import com.global.vms.model.ServiceResponse;
import com.global.vms.util.WebUtils;

@Component
public class VMSInterceptor implements HandlerInterceptor {
	private final Logger log = LoggerFactory.getLogger(VMSInterceptor.class);
	@Autowired
	Environment env;

	@Autowired
	TokenUtils tokenUtils;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Map<String, String> reqheader = new HashMap<>();
		Enumeration<String> allHeaders = request.getHeaderNames();
		while (allHeaders.hasMoreElements()) {
			String headerName = allHeaders.nextElement();
			reqheader.put(headerName, request.getHeader(headerName));
		}
		log.info("Request Header: {}", reqheader);
		String token = request.getHeader(env.getProperty("key.header.token", "access-token"));
		if (token == null || !tokenUtils.verifyToken(token, WebUtils.getIp(request))) {
			ServiceResponse<Object> unauthorizedResponse = new ServiceResponse<>(
					env.getProperty("status.unauthorized", Integer.class), env.getProperty("message.unauthorized"),
					null);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(new ObjectMapper().writeValueAsString(unauthorizedResponse));
			return false;
		}

		response.setHeader("Access-Control-Allow-Origin",
				request.getHeader("Origin") == null ? request.getHeader("Origin") : request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
		reqheader = new HashMap<>();
		allHeaders = request.getHeaderNames();
		while (allHeaders.hasMoreElements()) {
			String headerName = allHeaders.nextElement();
			reqheader.put(headerName, request.getHeader(headerName));
		}
		log.info("Request Header at end of interceptor: {}", reqheader);
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
