package hu.neuron.imaginer.user;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.user.service.UserService;
import hu.neuron.imaginer.user.vo.UserVO;

@SessionScoped
@ManagedBean(name = "userManagedBean")
public class UserManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(UserManagedBean.class.getName());

	@ManagedProperty("#{userServiceImpl}")
	private UserService userService;

	private UserVO actualUser;

	@PostConstruct
	public void init() {
		
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

	public String logout() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
		return "/public/login.xhtml?faces-redirect=true";
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

	public String getVisibleName() {
		if (StringUtils.isEmpty(actualUser.getFirstName())) {
			return actualUser.getFirstName();
		}
		return actualUser.getUsername();
	}
}