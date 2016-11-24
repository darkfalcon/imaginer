package hu.neuron.imaginer.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import hu.neuron.imaginer.entity.user.UserVerificationToken;
import hu.neuron.imaginer.exception.ApplicationException;
import hu.neuron.imaginer.exception.ErrorType;
import hu.neuron.imaginer.repository.user.UserRepository;
import hu.neuron.imaginer.repository.user.UserVerificationTokenRepository;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserRegistrationVO;
import hu.neuron.imaginer.vo.user.UserVO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserVerificationTokenRepository userVerificationTokenRepository;

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
		User userEntity = new DozerBeanMapper().map(user, User.class);
		userEntity.setActivated(Boolean.FALSE);
		userEntity.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		User savedUser = userRepository.save(userEntity);
		if (savedUser != null) {
			UserVerificationToken verificationToken = generateUserVerificationToken(savedUser);
			
		} else {
			throw new ApplicationException(ErrorType.REGISTRATION_ERROR,
					"Failed to register user: " + user.getUsername());
		}
	}

	private UserVerificationToken generateUserVerificationToken(User user) throws ApplicationException {
		String token = UUID.randomUUID().toString();
		UserVerificationToken savedUserToken = userVerificationTokenRepository.save(new UserVerificationToken(token, user));
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
}
