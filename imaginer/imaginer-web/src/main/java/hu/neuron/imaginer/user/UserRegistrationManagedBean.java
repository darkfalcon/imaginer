package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.user.service.UserService;
import hu.neuron.imaginer.user.vo.UserRegistrationVO;

@ViewScoped
@ManagedBean
public class UserRegistrationManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger("RegisterUserManagedBean");

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserRegistrationVO userToRegister;
	private boolean registrationFormVisible;

	public void prepareUserRegistration(AjaxBehaviorEvent action) {
		this.userToRegister = new UserRegistrationVO();
		this.registrationFormVisible = Boolean.TRUE;
	}

	public String registerUser() {
		try {
			userService.registerUser(userToRegister);
			return "successful-registration?faces-redirect=true&name=" + userToRegister.getFirstName() + " "
					+ userToRegister.getLastName() + "&email=" + userToRegister.getEmailAddress();
		} catch (ApplicationException e) {
			return "error";
		}
	}
	
	public void hideRegistrationForm(AjaxBehaviorEvent action) {
		this.registrationFormVisible = Boolean.FALSE;
	}

	public UserRegistrationVO getUserToRegister() {
		return userToRegister;
	}

	public void setUserToRegister(UserRegistrationVO userToRegister) {
		this.userToRegister = userToRegister;
	}

	public boolean isRegistrationFormVisible() {
		return registrationFormVisible;
	}

	public void setRegistrationFormVisible(boolean registrationFormVisible) {
		this.registrationFormVisible = registrationFormVisible;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
