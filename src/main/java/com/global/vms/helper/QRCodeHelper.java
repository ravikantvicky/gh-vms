package com.global.vms.helper;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.global.vms.exception.VMSException;
import com.global.vms.service.TextCryptService;
import com.global.vms.util.AppConstants;

@Service
public class QRCodeHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TextCryptService textCryptService;
	@Autowired
	Environment env;

	public byte[] getQRCodeImage(String text, int width, int height) {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0); /* default = 4 */
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] getQRCodeImageForVisitor(long visitorId) throws VMSException {
		try {
			return getQRCodeImage(textCryptService.encryptText(String.valueOf(visitorId)), AppConstants.QR_CODE_SIZE,
					AppConstants.QR_CODE_SIZE);
		} catch (Exception e) {
			log.error("Error in getQRCodeImageForVisitor", e);
			throw new VMSException(env.getProperty("errormsg.generateQRCode"));
		}
	}
}
