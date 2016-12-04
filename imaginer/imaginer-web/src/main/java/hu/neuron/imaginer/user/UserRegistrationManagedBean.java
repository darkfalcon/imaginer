package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.user.service.UserService;
import hu.neuron.imaginer.user.vo.UserRegistrationVO;

@ViewScoped
@ManagedBean
public class UserRegistrationManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationManagedBean.class.getName());

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	@ManagedProperty(value = "#{authenticationProviderImpl}")
	private AuthenticationProvider authenticationProvider;

	private UserRegistrationVO userToRegister;

	@PostConstruct
	public void init() {
		userToRegister = new UserRegistrationVO();
	}

	public String registerUser() {
		try {
			userService.registerUser(userToRegister);
			authenticateUser(userToRegister.getUsername(), userToRegister.getPassword());
			return "successful-registration";
		} catch (ApplicationException e) {
			return null;
		}
	}
	
	private boolean authenticateUser(String username, String password) {
        try {
        	Authentication authenticationResponseToken = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password ));
            SecurityContextHolder.getContext().setAuthentication(authenticationResponseToken);
            if (authenticationResponseToken.isAuthenticated()) {
                return Boolean.TRUE;
            }
        } catch (BadCredentialsException badCredentialsException) {
            FacesMessage facesMessage =
                new FacesMessage("Login Failed: please check your username/password and try again.");
            FacesContext.getCurrentInstance().addMessage(null,facesMessage);
        } catch (LockedException lockedException) {
            FacesMessage facesMessage =
                new FacesMessage("Account Locked: please contact your administrator.");
            FacesContext.getCurrentInstance().addMessage(null,facesMessage);
        } catch (DisabledException disabledException) {
            FacesMessage facesMessage =
                new FacesMessage("Account Disabled: please contact your administrator.");
            FacesContext.getCurrentInstance().addMessage(null,facesMessage);
        }

        return false;
	}

	public UserRegistrationVO getUserToRegister() {
		return userToRegister;
	}

	public void setUserToRegister(UserRegistrationVO userToRegister) {
		this.userToRegister = userToRegister;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}
}
