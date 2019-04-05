package com.global.vms.model;

public class RegVisitorResponse {
	private boolean isError;
	private String errorMsg;
	private boolean isSentForVerification;
	private VisitorProfileResponse profile;

	public RegVisitorResponse() {
	}

	public RegVisitorResponse(boolean isError, String errorMsg, boolean isSentForVerification,
			VisitorProfileResponse profile) {
		this.isError = isError;
		this.errorMsg = errorMsg;
		this.isSentForVerification = isSentForVerification;
		this.profile = profile;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isSentForVerification() {
		return isSentForVerification;
	}

	public void setSentForVerification(boolean isSentForVerification) {
		this.isSentForVerification = isSentForVerification;
	}

	public VisitorProfileResponse getProfile() {
		return profile;
	}

	public void setProfile(VisitorProfileResponse profile) {
		this.profile = profile;
	}

}
