package hu.neuron.imaginer.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.util.ValidationPropertyUtil;
import hu.neuron.imaginer.validation.service.ValidatorService;
import hu.neuron.imaginer.validation.vo.ValidationRequestVO;
import hu.neuron.imaginer.validation.vo.ValidationResultVO;

@Service
public class ValidatorServiceImpl implements ValidatorService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ValidatorServiceImpl.class.getName());

	public ValidationResultVO validateInput(ValidationRequestVO request) {

		try {
			String regex = ValidationPropertyUtil.getValidatorRegex(request.getType());

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request.getValue());
			if (matcher.matches()) {
				return new ValidationResultVO(Boolean.TRUE);
			} else {
				return new ValidationResultVO(Boolean.FALSE);
			}
		} catch (Exception e) {
			logger.error("Validation failed", e);
			return new ValidationResultVO(Boolean.FALSE);
		}
	}

}
