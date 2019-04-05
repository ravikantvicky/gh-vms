package com.global.vms.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	@Autowired
	Environment env;

	@Autowired
	TokenUtils tokenUtils;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader(env.getProperty("key.header.token", "access-token"));
		if (token == null || !tokenUtils.verifyToken(token, WebUtils.getIp(request))) {
		ServiceResponse<Object> unauthorizedResponse = new ServiceResponse<>(
				env.getProperty("status.unauthorized", Integer.class), env.getProperty("message.unauthorized"), null);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(unauthorizedResponse));
		return false;
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
