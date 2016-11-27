package hu.neuron.imaginer.validation.service;

import hu.neuron.imaginer.validation.vo.ValidationRequestVO;
import hu.neuron.imaginer.validation.vo.ValidationResultVO;

public interface ValidatorService {

	public ValidationResultVO validateInput(ValidationRequestVO request);
}
