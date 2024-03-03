package com.davidrodriguez.projecttf.service;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable, S> {

  T findOne(final S id);

  List<T> findAll();

  T create(final T entity);

  T update(final T entity);

  void delete(final T entity);

  boolean deleteById(final S id);

}
