package hu.neuron.imaginer.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static ErrorType errorType;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public ApplicationException(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(ErrorType errorType, String message) {
		super(message);
	}

	public static ErrorType getErrorType() {
		return errorType;
	}
}
