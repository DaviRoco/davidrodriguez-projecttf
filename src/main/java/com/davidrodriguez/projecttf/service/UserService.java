package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.entity.User;
import com.davidrodriguez.projecttf.repository.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User, Long> {
  private final UserRepository userRepository;
  public UserService(UserRepository userRepository){ this.userRepository = userRepository; }
  @Override
  protected CrudRepository<User, Long> getRepository() {
    return userRepository;
  }

  public User create(UserDto entity) {
    User newUser = User.builder().
        firstName(entity.getFirstName())
        .lastNames(entity.getLastNames())
        .email(entity.getEmail())
        .phone(entity.getPhone())
        .password(entity.getPassword())
        .age(entity.getAge())
        .state("Activo").build();
    return super.create(newUser);
  }

  public User update(User existingUser, UserDto userDto){
    existingUser.setFirstName(userDto.getFirstName());
    existingUser.setLastNames(userDto.getLastNames());
    existingUser.setEmail(userDto.getEmail());
    existingUser.setPhone(userDto.getPhone());
    existingUser.setPassword(userDto.getPassword());
    existingUser.setAge(userDto.getAge());
    existingUser.setState(userDto.getState());
    return userRepository.save(existingUser);
  }
  public User changeUserState(User existingUser) {
    String stateChangeEnumeration;
    if (existingUser.getState().equals("Inactivo")){
      stateChangeEnumeration = "Activo";
      existingUser.setState(stateChangeEnumeration);
    } else {
      stateChangeEnumeration = "Inactivo";
      existingUser.setState(stateChangeEnumeration);
    }
    return userRepository.save(existingUser);
  }
  public boolean delete(UserDto userDto) {
    userRepository.deleteById(userDto.getId());
    return true;
  }
}
