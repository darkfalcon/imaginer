package hu.neuron.imaginer.validation.vo;

public class ValidationResult {

	private boolean valid;

	public ValidationResult() {
		super();
	}

	public ValidationResult(boolean valid) {
		super();
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
