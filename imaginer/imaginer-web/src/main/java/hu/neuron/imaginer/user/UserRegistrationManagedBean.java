package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserRegistrationVO;

@ViewScoped
@ManagedBean
public class UserRegistrationManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger("RegisterUserManagedBean");

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserRegistrationVO userToRegister;
	private boolean registrationFormVisible;
	
	private String password;
	private String confirmPassword;

	public void prepareUserRegistration(AjaxBehaviorEvent action) {
		this.userToRegister = new UserRegistrationVO();
		this.setRegistrationFormVisible(Boolean.TRUE);
	}

	public String registerUser() {
		try {
			userToRegister.setPassword(password);
			userService.registerUser(userToRegister);
			return "successful-registration?faces-redirect=true&name=" + userToRegister.getFirstName() + " "
					+ userToRegister.getLastName() + "&email=" + userToRegister.getEmailAddress();
		} catch (ApplicationException e) {
			return "error";
		}
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setRegistrationFormVisible(boolean registrationFormVisible) {
		this.registrationFormVisible = registrationFormVisible;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
