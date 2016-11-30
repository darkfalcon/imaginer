package hu.neuron.imaginer.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.neuron.imaginer.entity.user.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
	public UserGroup findByCode(String code);
}
