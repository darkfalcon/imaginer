package hu.neuron.imaginer.service;

import java.util.List;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.vo.user.UserVO;

public interface UserService {
	
	public List<UserVO> findAllUsers() throws ApplicationException;
	
	public UserVO findUserById(Long id) throws ApplicationException;
	
	public UserVO findUserByUserName(String username) throws ApplicationException;
	
	public UserVO findUserByEmailAddress(String emailAddress) throws ApplicationException;
	
	public boolean registerUser(UserVO user) throws ApplicationException;
}
