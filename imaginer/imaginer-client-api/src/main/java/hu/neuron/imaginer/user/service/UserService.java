package hu.neuron.imaginer.user.service;

import java.util.List;

import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.user.vo.UserGroupVO;
import hu.neuron.imaginer.user.vo.UserLoginHistoryVO;
import hu.neuron.imaginer.user.vo.UserRegistrationVO;
import hu.neuron.imaginer.user.vo.UserVO;

public interface UserService {
	
	public List<UserVO> findAllUsers() throws ApplicationException;
	
	public UserVO findUserById(Long id) throws ApplicationException;
	
	public UserVO findUserByUserName(String username) throws ApplicationException;
	
	public UserVO findUserByEmailAddress(String emailAddress) throws ApplicationException;
	
	public void registerUser(UserRegistrationVO user) throws ApplicationException;
	
	public void activateUser(String token) throws ApplicationException;
	
	public List<UserGroupVO> getUserGroupsForUser(String username) throws ApplicationException;
	
	public void createLoginHistoryEntry(String username) throws ApplicationException;
	
	public UserLoginHistoryVO getUserLoginHistory(String username) throws ApplicationException;
	
	public List<UserLoginHistoryVO> getUsersLoginHistory() throws ApplicationException;
	
}
