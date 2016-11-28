package hu.neuron.imaginer.validation.service;

import hu.neuron.imaginer.validation.vo.ValidationRequest;
import hu.neuron.imaginer.validation.vo.ValidationResult;

public interface ValidatorService {

	public ValidationResult validateInput(ValidationRequest request);
}
