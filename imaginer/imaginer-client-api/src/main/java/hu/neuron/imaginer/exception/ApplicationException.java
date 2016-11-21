package hu.neuron.imaginer.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private ErrorType errorType;

	public ApplicationException(ErrorType errorType, String message, Throwable cause) {
		super(message, cause);
		this.errorType = errorType;
	}

	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
