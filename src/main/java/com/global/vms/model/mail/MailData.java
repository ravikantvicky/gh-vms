package com.global.vms.model.mail;

import java.util.List;

public class MailData {
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private String mailBody;
	private List<MailAttachment> inlineAttachments;
	private List<MailAttachment> fileAttachments;

	public MailData() {
	}

	public MailData(List<String> to, List<String> cc, List<String> bcc, String subject, String mailBody,
			List<MailAttachment> inlineAttachments, List<MailAttachment> fileAttachments) {
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.mailBody = mailBody;
		this.inlineAttachments = inlineAttachments;
		this.fileAttachments = fileAttachments;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public List<MailAttachment> getInlineAttachments() {
		return inlineAttachments;
	}

	public void setInlineAttachments(List<MailAttachment> inlineAttachments) {
		this.inlineAttachments = inlineAttachments;
	}

	public List<MailAttachment> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(List<MailAttachment> fileAttachments) {
		this.fileAttachments = fileAttachments;
	}
}
