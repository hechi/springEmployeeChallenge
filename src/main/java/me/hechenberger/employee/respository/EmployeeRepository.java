package me.hechenberger.employee.respository;

import me.hechenberger.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  /**
   * find an employee his email address
   *
   * @param email to search for
   * @return the employee with the email address or null if non was found
   */
  Employee findByEmail(String email);

  /**
   * check if an email address of an employee exists
   *
   * @param email to search for
   * @return true if email exists, otherwise return false
   */
  boolean existsByEmail(String email);
}
