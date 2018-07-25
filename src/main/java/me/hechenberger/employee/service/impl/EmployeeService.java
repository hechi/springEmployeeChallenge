package me.hechenberger.employee.service.impl;

import me.hechenberger.employee.model.Employee;
import me.hechenberger.employee.respository.EmployeeRepository;
import me.hechenberger.employee.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: provide exceptions
 * This service handel the connection to the repository
 */
@Service
public class EmployeeService implements IEmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;


  /**
   * Save an employee if the email does not exists in the system
   *
   * @param employee the new employee
   * @return null if the employee with the email exists, otherwise return the saved employee
   */
  //TODO throw error if employee esists with the same email
  @Override
  public Employee save(Employee employee) {
    if (employeeRepository.existsByEmail(employee.getEmail())) {
      return null;
    }
    return employeeRepository.save(employee);
  }

  /**
   * Return a list of all employees
   *
   * @return list of employees or an empty list if there is no employee
   */
  @Override
  public List<Employee> getAll() {
    return employeeRepository.findAll();
  }

  /**
   * Find an employee by id
   *
   * @param id of an employee
   * @return null if the id can not be found otherwise the corresponding employee
   */
  @Override
  public Employee getByIdx(Long id) {
    // TODO: probably throw error
    if (employeeRepository.existsById(id)) {
      return employeeRepository.findById(id).get();
    }
    return null;
  }

  /**
   * Update the fields of an employee. Do not allow an updated if employee does not exists and
   * do not updated the email address if another employee exists with the same email address.
   *
   * @param employeeToUpdate employee with new field values
   * @return corresponding employee or null if employee does not exists or tries to change his email address to an existing one
   */
  // TODO: probably throw error
  @Override
  public Employee update(Employee employeeToUpdate) {
    //TODO: ask should i save an employee if it does not exists?
    //TODO: only allow changing of the email address if it does not exists in the system
    if (employeeToUpdate.getId() != null
            && employeeRepository.existsById(employeeToUpdate.getId())) {
      Employee checkMail = employeeRepository.findByEmail(employeeToUpdate.getEmail());
      if (checkMail.getId() == employeeToUpdate.getId()) {
        return employeeRepository.save(employeeToUpdate);
      }
      return null;
    }
    return null;
  }

  /**
   * Remove an employee. If the employee does not exists, do nothing.
   *
   * @param employee
   */
  @Override
  public void remove(Employee employee) {
    employeeRepository.delete(employee);
  }
}
