package hu.neuron.imaginer.user;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserVO;

@Component
@ManagedBean
@RequestScoped
public class UserManagedBean {
	private String code;

	@Autowired
	UserService userService;

	public void addItem() {
		List<UserVO> allUser = userService.findAllUsers();
		for (UserVO userVO : allUser) {
			System.out.println(userVO.toString());
		}
		UserVO user = userService.findUserById(1l);
		System.out.println("User by id=1: " + user.toString());
		UserVO userVO = new UserVO();
		userVO.setUsername("zsoltty91");
		userVO.setFirstName("Zsolt");
		userVO.setLastName("Fehérvári");
		userVO.setEmailAddress("zsoltty91@freemail.hu");
		Instant birthDate = LocalDateTime.of(1991, Month.NOVEMBER, 20, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
		userVO.setBirthDate(Date.from(birthDate));
		boolean registerUser = userService.registerUser(userVO);
		if (registerUser == Boolean.TRUE) {
			System.out.println("Mentes sikeres");
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
