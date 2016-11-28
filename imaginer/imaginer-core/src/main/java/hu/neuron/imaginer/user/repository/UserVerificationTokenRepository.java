package hu.neuron.imaginer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.neuron.imaginer.entity.user.UserVerificationToken;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {
	
	public UserVerificationToken findByToken(String token);
}
