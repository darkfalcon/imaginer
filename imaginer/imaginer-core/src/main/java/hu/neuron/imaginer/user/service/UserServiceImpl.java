package hu.neuron.imaginer.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.neuron.imaginer.entity.user.User;
import hu.neuron.imaginer.entity.user.UserGroup;
import hu.neuron.imaginer.entity.user.UserLoginHistory;
import hu.neuron.imaginer.entity.user.UserVerificationToken;
import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.exception.ErrorType;
import hu.neuron.imaginer.user.repository.UserGroupRepository;
import hu.neuron.imaginer.user.repository.UserLoginHistoryRepository;
import hu.neuron.imaginer.user.repository.UserRepository;
import hu.neuron.imaginer.user.repository.UserVerificationTokenRepository;
import hu.neuron.imaginer.user.vo.UserGroupVO;
import hu.neuron.imaginer.user.vo.UserLoginHistoryVO;
import hu.neuron.imaginer.user.vo.UserRegistrationVO;
import hu.neuron.imaginer.user.vo.UserVO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private UserVerificationTokenRepository userVerificationTokenRepository;

	@Autowired
	private UserLoginHistoryRepository userLoginHistoryRepository;

	Logger logger = LoggerFactory.getLogger("UserServiceImpl");

	public List<UserVO> findAllUsers() throws ApplicationException {
		final List<User> users = userRepository.findAll();
		final List<UserVO> usersVOs;

		if (users.size() > 0) {
			usersVOs = new ArrayList<UserVO>(users.size());
			for (User user : users) {
				usersVOs.add(new DozerBeanMapper().map(user, UserVO.class));
			}
			return usersVOs;
		} else {
			logger.info("No users found!");
			return Collections.emptyList();
		}
	}

	public UserVO findUserById(Long id) throws ApplicationException {
		User userById = userRepository.findById(id);
		if (userById != null) {
			return new DozerBeanMapper().map(userById, UserVO.class);
		} else {
			throw new ApplicationException(ErrorType.NO_USER_WITH_ID,
					"There is no user found with this id: " + id + " in the database!");
		}
	}

	public UserVO findUserByUserName(String username) throws ApplicationException {
		User userByUsername = userRepository.findByUsername(username);
		if (userByUsername != null) {
			return new DozerBeanMapper().map(userByUsername, UserVO.class);
		} else {
			throw new ApplicationException(ErrorType.NO_USER_WITH_USERNAME,
					"There is no user found with this username: " + username + " in the database!");
		}
	}

	public UserVO findUserByEmailAddress(String emailAddress) throws ApplicationException {
		User userByEmailAddress = userRepository.findByEmailAddress(emailAddress);
		if (userByEmailAddress != null) {
			return new DozerBeanMapper().map(userByEmailAddress, UserVO.class);
		} else {
			throw new ApplicationException(ErrorType.NO_USER_WITH_EMAIL,
					"There is no user found with this email address: " + emailAddress + " in the database!");
		}
	}

	public void registerUser(UserRegistrationVO user) throws ApplicationException {
		try {
			User userEntity = new DozerBeanMapper().map(user, User.class);
			userEntity.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			List<UserGroup> userGroups = new ArrayList<>();
			userGroups.add(userGroupRepository.findByCode("USER"));
			userEntity.setUserGroups(userGroups);
			userRepository.save(userEntity);
		} catch (Throwable t) {
			throw new ApplicationException(ErrorType.REGISTRATION_ERROR,
					"Failed to register user: " + user.getUsername(), t);
		}
	}

	private UserVerificationToken createUserVerificationToken(User user) throws ApplicationException {
		String token = UUID.randomUUID().toString();
		UserVerificationToken savedUserToken = userVerificationTokenRepository
				.save(new UserVerificationToken(token, user));
		if (savedUserToken == null) {
			throw new ApplicationException(ErrorType.TOKEN_CREATION_ERROR,
					"Failed to save user verification token for user: " + user.getUsername());
		}
		return savedUserToken;
	}

	public void activateUser(String token) throws ApplicationException {
		UserVerificationToken userToken = userVerificationTokenRepository.findByToken(token);
		if (userToken != null) {
			userToken.getUser().setActivated(Boolean.TRUE);
			userRepository.save(userToken.getUser());
			userVerificationTokenRepository.delete(userToken);
		} else {
			throw new ApplicationException(ErrorType.TOKEN_NOT_FOUND, "There is no such token: " + token);
		}
	}

	@Override
	public List<UserGroupVO> getUserGroupsForUser(String username) throws ApplicationException {
		return findUserByUserName(username).getUserGroups();
	}

	@Override
	public void createLoginHistoryEntry(String username) throws ApplicationException {
		User user = userRepository.findByUsername(username);
		UserLoginHistory history = new UserLoginHistory();
		history.setLoginDate(new Date());
		history.setUser(user);
		userLoginHistoryRepository.save(history);
	}

	@Override
	public UserLoginHistoryVO getUserLoginHistory(String username) throws ApplicationException {
		UserLoginHistoryVO result = new UserLoginHistoryVO();
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new ApplicationException(ErrorType.NO_USER_WITH_USERNAME, "There is no such user in the database!");
		}
		result.setEmailAddress(user.getEmailAddress());
		List<UserLoginHistory> histories = userLoginHistoryRepository.findByUserIds(user.getId());
		if (!histories.isEmpty()) {
			for (UserLoginHistory userLoginHistory : histories) {
				result.getLoginDates().add(userLoginHistory.getLoginDate());
			}
		}
		return result;
	}

	@Override
	public List<UserLoginHistoryVO> getUsersLoginHistory() throws ApplicationException {
		List<UserLoginHistory> histories = userLoginHistoryRepository.findAll();
		if (histories.isEmpty()) {
			return Collections.emptyList();
		} else {
			Map<String, UserLoginHistoryVO> historiesPerUser = new HashMap<>();
			for (UserLoginHistory userLoginHistory : histories) {
				String email = userLoginHistory.getUser().getEmailAddress();
				UserLoginHistoryVO historyVO = historiesPerUser.get(email);
				if (historyVO != null) {
					historyVO.getLoginDates().add(userLoginHistory.getLoginDate());
				} else {
					historyVO = new UserLoginHistoryVO();
					historyVO.setEmailAddress(email);
					historyVO.getLoginDates().add(userLoginHistory.getLoginDate());
					historiesPerUser.put(email, historyVO);
				}
			}
			return new ArrayList<>(historiesPerUser.values());
		}
	}
}
