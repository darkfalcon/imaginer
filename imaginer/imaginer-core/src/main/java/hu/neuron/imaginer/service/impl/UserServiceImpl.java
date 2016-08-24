package hu.neuron.imaginer.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.neuron.imaginer.entity.user.User;
import hu.neuron.imaginer.repository.user.UserRepository;
import hu.neuron.imaginer.service.UserService;
import hu.neuron.imaginer.vo.user.UserVO;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	public List<UserVO> findAllUsers() {
		final List<User> users = userRepository.findAll();
		final List<UserVO> usersVOs;

		if (users.size() > 0) {
			usersVOs = new ArrayList<UserVO>(users.size());
			for (User user : users) {
				usersVOs.add(new DozerBeanMapper().map(user, UserVO.class));
			}
			return usersVOs;
		} else {
			return Collections.emptyList();
		}
	}

	public UserVO findUserById(Long id) {
		User userById = userRepository.findById(id);
		if (userById != null) {
			return new DozerBeanMapper().map(userById, UserVO.class);
		} else {
			throw new RuntimeException("There is no user found with this id: " + id + " in the database!");
		}
	}

	public boolean registerUser(UserVO user) {
		User userEntity = new DozerBeanMapper().map(user, User.class);
		User savedUser = userRepository.save(userEntity);
		return savedUser != null ? true : false;
	}

}
