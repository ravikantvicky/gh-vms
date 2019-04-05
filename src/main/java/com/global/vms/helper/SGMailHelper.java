package com.global.vms.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.global.vms.exception.VMSException;
import com.global.vms.model.mail.MailData;
import com.global.vms.util.ImageUtils;

@Service
public class SGMailHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	QRCodeHelper qrCodeHelper;

	@Autowired
	PdfHelper pdfHelper;

	@Autowired
	Environment env;

	public void sendMail(MailData mailData) throws VMSException {
		try {
			Email from = new Email("subhendu.r.mishra@gmail.com");
			Email to = new Email(mailData.getTo().get(0));
			Content content = new Content("text/html", mailData.getMailBody());
			Mail mail = new Mail(from, mailData.getSubject(), to, content);
			if (mailData.getInlineAttachments() != null)
				mailData.getInlineAttachments().forEach(data -> {
					if ((data.getFileData() == null || data.getFileData().length == 0) && data.getFilepath() != null
							&& !data.getFilepath().isEmpty()) {
						try {
							data.setFileData(Files.readAllBytes(new File(data.getFilepath()).toPath()));
						} catch (IOException e) {
							log.error("Error in attaching inline file", e);
						}
					}
					if (data.getFileData() != null && data.getFileData().length > 0) {
						try {
							Attachments att = new Attachments();
							att.setContentId(data.getLabel());
							att.setContent(ImageUtils.imageToBase64(data.getFileData()));
							att.setType(ImageUtils.getImageMimeType(data.getFileData()));
							att.setFilename(data.getLabel());
							att.setDisposition("inline");
							mail.addAttachments(att);
						} catch (Exception e) {
							log.error("Error in attaching inline file", e);
						}
					}
				});
			if (mailData.getFileAttachments() != null)
				mailData.getFileAttachments().forEach(data -> {
					if ((data.getFileData() == null || data.getFileData().length == 0) && data.getFilepath() != null
							&& !data.getFilepath().isEmpty()) {
						try {
							data.setFileData(Files.readAllBytes(new File(data.getFilepath()).toPath()));
						} catch (IOException e) {
							log.error("Error in attaching file", e);
						}
					}
					if (data.getFileData() != null && data.getFileData().length > 0) {
						try {
							Attachments att = new Attachments();
							att.setContentId(data.getLabel());
							att.setContent(ImageUtils.imageToBase64(data.getFileData()));
							att.setType(ImageUtils.getImageMimeType(data.getFileData()));
							att.setFilename(data.getLabel());
							att.setDisposition("attachment");
							mail.addAttachments(att);
						} catch (Exception e) {
							log.error("Error in attaching file", e);
						}
					}
				});
			SendGrid sg = new SendGrid(env.getProperty("mail.sendgrid.apiKey"));
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());

		} catch (Exception e) {
			log.error("Error in sending mail: ", e);
			throw new VMSException(env.getProperty("errormsg.sendmail"));
		}
	}
}
