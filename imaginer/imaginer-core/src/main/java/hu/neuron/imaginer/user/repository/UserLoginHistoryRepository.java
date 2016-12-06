package hu.neuron.imaginer.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.neuron.imaginer.entity.user.UserLoginHistory;

public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Long> {
	
	@Query("Select h from UserLoginHistory h where h.user.id = :id")
	public List<UserLoginHistory> findByUserIds(@Param("id") Long id);

}
