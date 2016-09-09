package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserVO;

@SessionScoped
@ManagedBean(name = "userInfoManagedBean")
public class UserInfoManagedBean implements Serializable {

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserVO actualUser;

	private static final long serialVersionUID = 1L;

	public void initUserInfo() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			if (username.contains("@")) {
				this.actualUser = userService.findUserByEmailAddress(username);
			} else {
				this.actualUser = userService.findUserByUserName(username);
			}
		} catch (ApplicationException e) {

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
