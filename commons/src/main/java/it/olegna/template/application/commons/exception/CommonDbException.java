package it.olegna.template.application.commons.exception;

public class CommonDbException extends Exception {

	private static final long serialVersionUID = -7675753989555594450L;

	public CommonDbException() {
		super();
	}

	public CommonDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CommonDbException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonDbException(String message) {
		super(message);
	}

	public CommonDbException(Throwable cause) {
		super(cause);
	}

}
