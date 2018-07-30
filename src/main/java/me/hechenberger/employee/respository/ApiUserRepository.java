package me.hechenberger.employee.respository;

import me.hechenberger.employee.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser,Long> {
  ApiUser findByUsername(String username);
}
