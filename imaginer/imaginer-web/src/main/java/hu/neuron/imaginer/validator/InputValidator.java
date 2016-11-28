package hu.neuron.imaginer.validator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import hu.neuron.imaginer.validation.service.ValidatorService;
import hu.neuron.imaginer.validation.vo.ValidationFieldType;
import hu.neuron.imaginer.validation.vo.ValidationRequest;
import hu.neuron.imaginer.validation.vo.ValidationResult;

@RequestScoped
@ManagedBean(name = "inputValidator")
public class InputValidator implements Validator {

	@ManagedProperty("#{validatorServiceImpl}")
	private ValidatorService validatorService;

	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object object) throws ValidatorException {
		String type = (String) component.getAttributes().get("type");
		ValidationResult result = validatorService
				.validateInput(new ValidationRequest(ValidationFieldType.valueOf(type), object.toString()));
		if (!result.isValid()) {
			throw new ValidatorException(new FacesMessage("field invalid"));
		}
	}

	public void setValidatorService(ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

}
