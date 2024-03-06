package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.entity.User;
import com.davidrodriguez.projecttf.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, Long> {
  private final UserRepository userRepository;
  public UserService(UserRepository userRepository){ this.userRepository = userRepository; }
  @Override
  protected CrudRepository<User, Long> getRepository() {
    return userRepository;
  }
}
