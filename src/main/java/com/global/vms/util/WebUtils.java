package com.global.vms.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.global.vms.exception.VMSException;

public class WebUtils {
	private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

	public static String getIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
			InetAddress localip = null;
			try {
				localip = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			remoteAddr = localip.getHostAddress();
		}
		return remoteAddr;
	}

	public static String getBaseUrl(HttpServletRequest req) throws VMSException {
		try {
			String scheme = req.getScheme(); // http
			String serverName = req.getServerName(); // hostname.com
			int serverPort = req.getServerPort(); // 80
			String contextPath = req.getContextPath(); // /mywebapp

			// Reconstruct original requesting URL
			StringBuilder url = new StringBuilder();
			url.append(scheme).append("://").append(serverName);

			if (serverPort != 80 && serverPort != 443) {
				url.append(":").append(serverPort);
			}
			url.append(contextPath);
			return url.toString();
		} catch (Exception e) {
			log.error("Error in getting base URL:", e);
			throw new VMSException("Unexpected error occurred !");
		}
	}
}
