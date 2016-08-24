package hu.neuron.imaginer.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import hu.neuron.imaginer.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@PersistenceContext
	private EntityManager em;

	public boolean addItem(String code) {
		
		Query createQuery = em.createQuery("SELECT username FROM User");
		for (Object object : createQuery.getResultList()) {
			System.out.println("Name:" + object);
		}
		
		System.out.println("This is the code nigga: " + code);
		return false;
	}

}
