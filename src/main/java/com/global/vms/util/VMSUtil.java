package com.global.vms.util;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VMSUtil {
	private static final Logger log = LoggerFactory.getLogger(VMSUtil.class);

	public static String formatStringDate(String date, String existingFormat, String desiredFormat) {
		log.debug("Formating date: {} having fromat: {} to format: {}", date, existingFormat, desiredFormat);
		try {
			SimpleDateFormat exDf = new SimpleDateFormat(existingFormat);
			SimpleDateFormat dsDf = new SimpleDateFormat(desiredFormat);
			String finalDt = dsDf.format(exDf.parse(date));
			log.debug("Formated Date: {}", finalDt);
			return finalDt;
		} catch (Exception e) {
			log.error("Error in formating date string", e);
		}
		return null;
	}

	public static String extractFirstName(String fullName) {
		if (fullName == null)
			return fullName;
		return fullName.split(" ")[0];
	}
}
