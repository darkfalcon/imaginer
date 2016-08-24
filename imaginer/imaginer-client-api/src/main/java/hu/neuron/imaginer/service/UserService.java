package hu.neuron.imaginer.service;

import java.util.List;

import hu.neuron.imaginer.vo.user.UserVO;

public interface UserService {
	
	public List<UserVO> findAllUsers();
	
	public UserVO findUserById(Long id);
	
	public boolean registerUser(UserVO user);
}
