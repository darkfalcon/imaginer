package hu.neuron.imaginer.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.service.UserService;

@RequestScoped
@ManagedBean
public class UserActivationManagedBean {

	Logger logger = LoggerFactory.getLogger("UserActivationManagedBean");

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	public String activateAccount() {
		try {
			String token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
			logger.debug("Activating user account with token: " + token);
			userService.activateUser(token);
			return "login";
		} catch (ApplicationException e) {
			return "errorPage";
		}
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
