package hu.neuron.imaginer.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.neuron.imaginer.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findAll();
	
	public User findById(Long id);

}
