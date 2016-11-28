package hu.neuron.imaginer.validation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.util.ValidationPropertyUtil;
import hu.neuron.imaginer.validation.service.ValidatorService;
import hu.neuron.imaginer.validation.vo.ValidationRequest;
import hu.neuron.imaginer.validation.vo.ValidationResult;

@Service
public class ValidatorServiceImpl implements ValidatorService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ValidatorServiceImpl.class.getName());

	public ValidationResult validateInput(ValidationRequest request) {

		try {
			String regex = ValidationPropertyUtil.getValidatorRegex(request.getType());

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request.getValue());
			if (matcher.matches()) {
				return new ValidationResult(Boolean.TRUE);
			} else {
				return new ValidationResult(Boolean.FALSE);
			}
		} catch (Exception e) {
			logger.error("Validation failed", e);
			return new ValidationResult(Boolean.FALSE);
		}
	}

}
