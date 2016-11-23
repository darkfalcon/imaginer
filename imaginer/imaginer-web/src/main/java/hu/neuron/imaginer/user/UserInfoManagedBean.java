package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserVO;

@SessionScoped
@ManagedBean(name = "userInfoManagedBean")
public class UserInfoManagedBean implements Serializable {

	Logger logger = LoggerFactory.getLogger("UserInfoManagedBean");

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserVO actualUser;

	private static final long serialVersionUID = 1L;

	public void initUserInfo() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			if (username.contains("@")) {
				this.actualUser = userService.findUserByEmailAddress(username);
			} else {
				this.actualUser = userService.findUserByUserName(username);
			}
		} catch (ApplicationException e) {
			logger.error("Failed to query user information for username: " + username, e);
		}
	}

	public UserVO getActualUser() {
		return actualUser;
	}

	public void setActualUser(UserVO actualUser) {
		this.actualUser = actualUser;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}