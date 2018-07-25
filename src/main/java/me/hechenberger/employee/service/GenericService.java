package me.hechenberger.employee.service;

import java.util.List;

/**
 * Interface defines all function that a usual restful service should provide
 *
 * @param <T>
 */
public interface GenericService<T> {
  T save(T element);

  List<T> getAll();

  T getByIdx(Long id);

  T update(T toUpdate);

  void remove(T element);
}
