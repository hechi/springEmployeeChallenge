package me.hechenberger.employee.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Interface defines all usual needed Restful operations
 *
 * @param <T>
 */
public interface IController<T> {

  ResponseEntity<List<T>> getAll();

  ResponseEntity<T> get(Long id);

  ResponseEntity<T> delete(T element);

  ResponseEntity<T> update(T element);

  ResponseEntity<T> create(T element);
}
