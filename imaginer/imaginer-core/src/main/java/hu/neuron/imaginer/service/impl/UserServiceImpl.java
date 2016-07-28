package hu.neuron.imaginer.service.impl;

import org.springframework.stereotype.Service;

import hu.neuron.imaginer.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	public boolean addItem(String code) {
		System.out.println("This is the code nigga: " + code);
		return false;
	}

}
