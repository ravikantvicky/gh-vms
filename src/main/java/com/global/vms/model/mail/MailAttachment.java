package com.global.vms.model.mail;

public class MailAttachment {
	private String label;
	private String filepath;
	private byte[] fileData;

	public MailAttachment() {
	}

	public MailAttachment(String label, String filepath, byte[] fileData) {
		this.label = label;
		this.filepath = filepath;
		this.fileData = fileData;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}
