package hu.neuron.imaginer.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.neuron.imaginer.service.UserService;

@Component
@ManagedBean
@RequestScoped
public class UserManagedBean {
	private String code;

	@Autowired
	UserService userService;

	public void addItem() {
		System.out.println("This code: " + code);
		userService.addItem(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
