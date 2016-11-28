package hu.neuron.imaginer.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.neuron.imaginer.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findAll();
	
	public User findById(Long id);
	
	public User findByUsername(String username);

	public User findByEmailAddress(String emailAddress);
}
