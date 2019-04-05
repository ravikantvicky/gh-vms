package com.global.vms.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
	private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

	public static byte[] base64ToImage(String base64Data) {
		return DatatypeConverter.parseBase64Binary(base64Data);
	}

	public static String imageToBase64(byte[] imageData) {
		return DatatypeConverter.printBase64Binary(imageData);
	}

	public static byte[] resizeImage(byte[] imageData, int scaledWidth, int scaledHeight) {
		try {
			BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageData));
			BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

			// scales the input image to the output image
			Graphics2D g2d = outputImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
			g2d.dispose();
			ByteArrayOutputStream outputImageBytes = new ByteArrayOutputStream();
			String[] mimeType = getImageMimeType(imageData).split("/");
			log.debug("Mime Type: {}", mimeType[1]);
			ImageIO.write(outputImage, mimeType[1], outputImageBytes);
			return outputImageBytes.toByteArray();
		} catch (Exception e) {
			log.error("Error in resizeImage", e);
			return null;
		}
	}

	public static String getImageMimeType(byte[] imageData) {
		try {
			InputStream is = new ByteArrayInputStream(imageData);
			return URLConnection.guessContentTypeFromStream(is);
		} catch (Exception e) {
			log.error("Error in retriving mime type", e);
			return null;
		}
	}
}
