package me.hechenberger.employee.service.impl;

import me.hechenberger.employee.service.GenericService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractGenericServiceImpl<T> implements GenericService<T> {

  public abstract JpaRepository<T,Long> getRepository();

  @Override
  public T save(T element) {
    return getRepository().save(element);
  }

  @Override
  public List<T> getAll() {
    return getRepository().findAll();
  }

  @Override
  public T getByIdx(Long id) {
    if(getRepository().existsById(id)){
      return getRepository().findById(id).get();
    }
    return null;
  }

  @Override
  public T update(T toUpdate) {
    return getRepository().save(toUpdate);
  }

  @Override
  public void remove(T element) {
    getRepository().delete(element);
  }
}

