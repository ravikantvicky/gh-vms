package com.global.vms.util;

import java.security.MessageDigest;
import java.util.List;

public class MessageUtils {
	public static String toSHA256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String getCommaSaperatedString(List<String> strings) {
		if (strings == null || strings.isEmpty())
			return "";
		if (strings.size() == 1)
			return strings.get(0);
		return String.join(", ", strings.subList(0, strings.size())) + " and " + strings.get(strings.size() - 1);
	}
}
