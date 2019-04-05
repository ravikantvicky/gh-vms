package com.global.vms.exception;

public class VMSException extends Exception {
	private static final long serialVersionUID = 1L;

	public VMSException() {
		super();
	}

	public VMSException(String message, Throwable t) {
		super(message, t);
	}

	public VMSException(String message) {
		super(message);
	}

	public VMSException(Throwable t) {
		super(t);
	}

}
