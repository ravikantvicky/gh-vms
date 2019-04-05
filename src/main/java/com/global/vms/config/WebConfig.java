package com.global.vms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	HandlerInterceptor vMSInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*registry.addInterceptor(vMSInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/", "/images/**",
				"/approve/**");*/
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
				.allowedHeaders("*");
	}
}
