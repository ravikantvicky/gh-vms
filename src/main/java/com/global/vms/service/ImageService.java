package com.global.vms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.global.vms.exception.VMSException;
import com.global.vms.repository.ImageRepository;
import com.global.vms.util.ImageUtils;

@Service
public class ImageService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	Environment env;

	public byte[] getImage(long imageId) throws VMSException {
		try {
			return ImageUtils.base64ToImage(imageRepository.fetchImage(imageId));
		} catch (VMSException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error in getImage service: ", e);
			throw new VMSException(env.getProperty("errormsg.generic"));
		}
	}
}
