package me.hechenberger.employee.controller.impl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.hechenberger.employee.controller.IApiUserController;
import me.hechenberger.employee.model.ApiUser;
import me.hechenberger.employee.service.IApiUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller which handel all incoming queries for the user endpoint.
 */
@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "User restful API. This endpoint allows you to register. All users are automatically enabled")
public class ApiUserController implements IApiUserController {

  //for logging output
  private Logger LOG = LoggerFactory.getLogger(ApiUserController.class);

  @Autowired
  private IApiUserService apiUserService;

  @Override
  public ResponseEntity<List<ApiUser>> getAll() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ApiUser> get(Long id) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ApiUser> delete(ApiUser element) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  public ResponseEntity<ApiUser> update(ApiUser element) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @Override
  @PostMapping
  @ApiOperation("Register a new user.")
  public ResponseEntity<ApiUser> create(@RequestBody ApiUser user) {
    LOG.debug("try to save user... " + user.getUsername());
    ApiUser apiUser = apiUserService.save(user);
    if (apiUser != null) {
      LOG.debug("User saved");
      return new ResponseEntity<>(apiUser, HttpStatus.CREATED);
    }
    LOG.debug("User not saved");
    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
  }
}
