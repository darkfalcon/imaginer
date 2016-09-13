package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserVO;

@ViewScoped
@ManagedBean
public class UserRegistrationManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger("RegisterUserManagedBean");

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserVO userToRegister;
	private boolean registrationFormVisible;

	public void prepareUserRegistration(AjaxBehaviorEvent action) {
		this.userToRegister = new UserVO();
		this.setRegistrationFormVisible(Boolean.TRUE);
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

	public UserVO getUserToRegister() {
		return userToRegister;
	}

	public void setUserToRegister(UserVO userToRegister) {
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
