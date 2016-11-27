package hu.neuron.imaginer.validation.vo;

public class ValidationRequestVO {

	private ValidationFieldType type;
	private String value;

	public ValidationRequestVO() {
		super();
	}

	public ValidationRequestVO(ValidationFieldType type, String value) {
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
