package hu.neuron.imaginer.validation.vo;

public class ValidationResultVO {

	private boolean valid;

	public ValidationResultVO() {
		super();
	}

	public ValidationResultVO(boolean valid) {
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
