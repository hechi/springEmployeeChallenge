package me.hechenberger.employee.controller.impl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.hechenberger.employee.controller.IController;
import me.hechenberger.employee.model.Employee;
import me.hechenberger.employee.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller which handel all incoming queries for the employee endpoint.
 */
@RestController
@RequestMapping(value = "/api/v1/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Employee restful API")
public class EmployeeController implements IController<Employee> {

  //for logging output
  private Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

  @Autowired
  private IEmployeeService employeeService;

  @Override
  @GetMapping
  @ApiOperation(value = "Returns list of all employee's.")
  public ResponseEntity<List<Employee>> getAll() {
    LOG.debug("get list of employee's");
    return new ResponseEntity<>(employeeService.getAll(), HttpStatus.OK);
  }

  @Override
  @GetMapping(value = "/{id}")
  @ApiOperation("Find an employee by id.")
  public ResponseEntity<Employee> get(@PathVariable Long id) {
    Employee employee = employeeService.getByIdx(id);
    if (employee != null) {
      LOG.debug("found employee with id: " + id);
      return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    LOG.debug("can not find employee with id: " + id);
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @Override
  @DeleteMapping
  @ApiOperation("Delete an employee.")
  public ResponseEntity<Employee> delete(@RequestBody Employee employeeToDelete) {
    Employee employee = employeeService.getByIdx(employeeToDelete.getId());
    if (employee != null) {
      LOG.debug("remove employee with id: " + employee.getId());
      employeeService.remove(employee);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    LOG.debug("can not delete employee with id: " + employeeToDelete.getId());
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @Override
  @PutMapping
  @ApiOperation("Update an employee.")
  public ResponseEntity<Employee> update(@RequestBody Employee element) {
    Employee employee = employeeService.update(element);
    if (employee != null) {
      LOG.debug("update employee");
      return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    LOG.debug("can not update employee");
    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
  }

  @Override
  @PostMapping
  @ApiOperation("Add a new employee.")
  public ResponseEntity<Employee> create(@RequestBody Employee element) {
    LOG.debug("try to save employee... " + element.getEmail());
    Employee employee = employeeService.save(element);
    if (employee != null) {
      LOG.debug("Employee saved");
      return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }
    LOG.debug("Employee not saved");
    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
  }
}
