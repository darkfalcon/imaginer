package hu.neuron.imaginer.validator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import hu.neuron.imaginer.util.MessageBundle;
import hu.neuron.imaginer.validation.service.ValidatorService;
import hu.neuron.imaginer.validation.vo.ValidationFieldType;
import hu.neuron.imaginer.validation.vo.ValidationRequest;
import hu.neuron.imaginer.validation.vo.ValidationResult;

@RequestScoped
@ManagedBean(name = "inputValidator")
public class InputValidator implements Validator {

	private static final String ERROR_MESSAGE_PREFIX = "imaginer.error.input";

	@ManagedProperty("#{validatorServiceImpl}")
	private ValidatorService validatorService;

	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object object) throws ValidatorException {
		String type = (String) component.getAttributes().get("type");
		String value = object.toString();
		if (type != null && !value.isEmpty()) {
			ValidationResult result = validatorService
					.validateInput(new ValidationRequest(ValidationFieldType.valueOf(type), object.toString()));
			if (!result.isValid()) {
				String message = MessageBundle.getBundle(ERROR_MESSAGE_PREFIX + '.' + type.toLowerCase());
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", message));
			}
		}
	}

	public void setValidatorService(ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

}
