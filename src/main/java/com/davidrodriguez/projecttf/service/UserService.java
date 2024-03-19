package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.entity.User;
import com.davidrodriguez.projecttf.repository.UserRepository;
import com.davidrodriguez.projecttf.utils.PasswordEncoder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService extends AbstractService<User, Long> {
  private final UserRepository userRepository;
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  @Override
  protected CrudRepository<User, Long> getRepository() {
    return userRepository;
  }

  public User create(UserDto entity) {
    User existingUser = userRepository.getUserByEmail(entity.getEmail());
    if (existingUser == null) {
      String encryptedPassword = PasswordEncoder.encodePassword(entity.getPassword());
      User newUser = User.builder().
              firstName(entity.getFirstName())
              .lastNames(entity.getLastNames())
              .email(entity.getEmail())
              .phone(entity.getPhone())
              .password(encryptedPassword)
              .age(entity.getAge())
              .state("Inactivo")
              .gender(entity.getGender()).build();
      return super.create(newUser);
    }
    return null;
  }

  public User update(User existingUser, UserDto userDto){
    if (userDto.getPassword() != null) {
      String encryptedPassword = PasswordEncoder.encodePassword(userDto.getPassword());
      existingUser.setPassword(encryptedPassword);
    }
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

  public User getUserByEmail(String email) {
      return userRepository.getUserByEmail(email);
  }

  public User login(String email, String password) {
    User existingUser = userRepository.getUserByEmail(email);
    if (existingUser != null && !Objects.equals(existingUser.getState(), "Inactivo")) {
      if (PasswordEncoder.checkPassword(password, existingUser.getPassword())) {
        return new User(0L, "", "", existingUser.getEmail(), "", "", 0, existingUser.getState(), "");
      }
      return null;
    }
    return null;
  }
}
