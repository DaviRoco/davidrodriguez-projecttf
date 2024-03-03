package com.davidrodriguez.projecttf.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService<T extends Serializable, S> implements IOperations<T, S> {

  @Transactional(readOnly = true)
  public T findOne(final S id) {
    return getRepository().findById(id).orElse(null);
  }

  @Transactional(readOnly = true)
  public List<T> findAll() {
    List<T> list = new ArrayList<>();
    getRepository().findAll().iterator().forEachRemaining(list::add);
    return list;
  }

  public T create(final T entity) {
    return getRepository().save(entity);
  }

  public T update(final T entity) {
    return getRepository().save(entity);
  }

  public void delete(final T entity) { getRepository().delete(entity);}

  public boolean deleteById(final S id){
    var entity = findOne(id);
    if (entity != null) {
      delete(entity);
      return true;
    }
    return false;
  }

  protected abstract CrudRepository<T, S> getRepository();
}
