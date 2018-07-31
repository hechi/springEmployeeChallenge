package me.hechenberger.employee.respository;

import me.hechenberger.employee.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser,Long> {
  /**
   * find an user by his username
   *
   * @param username to search for
   * @return the ApiUser with the given username or null if it was not found
   */
  ApiUser findByUsername(String username);
}
