package com.global.vms.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.global.vms.exception.VMSException;
import com.global.vms.model.mail.MailData;
import com.global.vms.util.ImageUtils;

@Service
public class EmailHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	QRCodeHelper qrCodeHelper;

	@Autowired
	PdfHelper pdfHelper;

	@Autowired
	Environment env;

	public void sendMail(MailData mail) throws VMSException {
		List<String> tempFiles = new ArrayList<>();
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "outlook.office365.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setJavaMailProperties(props);
			sender.setUsername(env.getProperty("mail.userName"));
			sender.setPassword(env.getProperty("mail.password"));

			MimeMessage message = sender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			if (mail.getTo() != null)
				helper.setTo(mail.getTo().toArray(new String[mail.getTo().size()]));
			if (mail.getCc() != null)
				helper.setCc(mail.getCc().toArray(new String[mail.getCc().size()]));
			if (mail.getBcc() != null)
				helper.setBcc(mail.getBcc().toArray(new String[mail.getBcc().size()]));
			helper.setFrom(env.getProperty("mail.fromName") + "<" + env.getProperty("mail.fromMailId") + ">");
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getMailBody(), true);

			if (mail.getInlineAttachments() != null)
				mail.getInlineAttachments().forEach(data -> {
					if (data.getFileData() != null && data.getFileData().length > 0) {
						try {
							String[] fileType = ImageUtils.getImageMimeType(data.getFileData()).split("/");
							String fileName = "tempFile" + UUID.randomUUID() + (tempFiles.size() + 1) + "."
									+ fileType[1];
							File file = new File(fileName);
							try (FileOutputStream fos = new FileOutputStream(file)) {
								fos.write(data.getFileData());
								tempFiles.add(file.getAbsolutePath());
							}

							FileSystemResource res = new FileSystemResource(new File(fileName));
							helper.addInline(data.getLabel(), res);
						} catch (Exception e) {
							log.error("Error in attaching inline file", e);
						}
					} else {
						try {
							helper.addInline(data.getLabel(), new FileSystemResource(new File(data.getFilepath())));
						} catch (MessagingException e) {
							log.error("Error in attaching inline file", e);
						}
					}
				});

			if (mail.getFileAttachments() != null)
				mail.getFileAttachments().forEach(data -> {
					if (data.getFileData() != null && data.getFileData().length > 0) {
						try {
							helper.addAttachment(data.getLabel(), new ByteArrayResource(data.getFileData()));
						} catch (MessagingException e) {
							log.error("Error in attaching inline file", e);
						}
					} else {
						try {
							helper.addAttachment(data.getLabel(), new FileSystemResource(new File(data.getFilepath())));
						} catch (MessagingException e) {
							log.error("Error in attaching inline file", e);
						}
					}
				});
			sender.send(message);
		} catch (Exception e) {
			log.error("Error in sending mail: ", e);
			throw new VMSException(env.getProperty("errormsg.sendmail"));
		} finally {
			tempFiles.forEach(file -> {
				try {
					Files.deleteIfExists(Paths.get(file));
				} catch (Exception e) {
					log.error("Error in removing file {}", file, e);
				}
			});
		}
	}
}
