package hu.neuron.imaginer.validation.vo;

public class ValidationRequest {

	private ValidationFieldType type;
	private String value;

	public ValidationRequest() {
		super();
	}

	public ValidationRequest(ValidationFieldType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	public ValidationFieldType getType() {
		return type;
	}

	public void setType(ValidationFieldType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
