package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.EmailMessage;
import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.entity.User;
import com.davidrodriguez.projecttf.service.EmailService;
import com.davidrodriguez.projecttf.service.UserService;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {
  private final UserService userService;

  private final EmailService emailService;

  public UserController(UserService userService, EmailService emailService) {
    this.userService = userService;
    this.emailService = emailService;
  }

  private final ModelMapper modelMapper = new ModelMapper();
  @GetMapping("/user-by-email/{email}")
  public ResponseEntity<UserDto> getByEmail(@PathVariable String email) {
    var type = new TypeToken<UserDto>() {}.getType();
    var user = userService.getUserByEmail(email);
    if (user != null) {
      return ResponseEntity.ok(modelMapper.map(user, type));
    }
    return null;
  }
  @GetMapping("/user")
  public ResponseEntity<List<UserDto>> get() {
    var listType = new TypeToken<List<UserDto>>() {}.getType();
    var list = (List<UserDto>) modelMapper.map(userService.findAll(), listType);
    return ResponseEntity.ok(list);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    var type = new TypeToken<UserDto>() {}.getType();
    User user = userService.findOne(id);
    if (user != null) {
      UserDto userDto = modelMapper.map(userService.findOne(id), type);
      return ResponseEntity.ok(userDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @PostMapping("/user")
  public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
    User createdUser = userService.create(userDto);
    if (createdUser != null) {
//      EmailMessage introMessage = new EmailMessage(createdUser.getEmail(), "david@rodriguezcoto.com", "Bienvenido a Farmaticas", "Bienvenido a Farmaticas, por favor ingrese a cambiar su contraseña en el siguiente <a href='http://localhost:4200/#/change-password?email=" + userDto.getEmail() + "'> link </a>");
      emailService.sendIntroEmail(userDto.getEmail(), userDto);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
  @PutMapping("/user")
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
    var type = new TypeToken<UserDto>() {}.getType();
    User existingUser = userService.findOne(userDto.getId());
    if (existingUser != null){
      User updatedUser = userService.update(existingUser, userDto);
      UserDto updatedUserDto = modelMapper.map(updatedUser, type);
      return ResponseEntity.ok(updatedUserDto);
    }
    return ResponseEntity.notFound().build();
  }
  @PutMapping("/user/state-change")
  public ResponseEntity<UserDto> changeStateUser(@RequestBody UserDto userDto) {
    var type = new TypeToken<UserDto>() {}.getType();
    User existingUser = userService.findOne(userDto.getId());
    if (existingUser != null) {
      User updatedUser = userService.changeUserState(existingUser);
      UserDto updatedUserDto = modelMapper.map(updatedUser, type);
      return ResponseEntity.ok(updatedUserDto);
    }
    return ResponseEntity.notFound().build();
  }
  @DeleteMapping("/user")
  public ResponseEntity<String> deleteUser(@RequestBody UserDto userDto) {
    User existingUser = userService.findOne(userDto.getId());
    if (existingUser != null) {
      boolean deleted = userService.delete(userDto);
      if (deleted) {
        return ResponseEntity.ok("User got deleted successfully");
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/user/login")
  public ResponseEntity<UserDto> loginUser(@RequestBody UserDto userDto) {
    var type = new TypeToken<UserDto>() {}.getType();
    String email = userDto.getEmail();
    User loggedInUser = userService.login(email, userDto.getPassword());
    if (loggedInUser != null) {
      return ResponseEntity.ok(modelMapper.map(loggedInUser, type));
    }
    return null;
  }

  @PostMapping("/user/reset-password")
  public void sendEmail(@RequestBody UserDto userDto) {
//    EmailMessage introMessage = new EmailMessage(userDto.getEmail(), "david@rodriguezcoto.com", "Recuperación de contraseña: Farmaticas", "Bienvenido a Farmaticas, por favor ingrese a cambiar su contraseña en el siguiente <a href='http://localhost:4200/#/change-password?email=" + userDto.getEmail() + "'> link </a>");
    emailService.sendRecoveryEmail(userDto.getEmail(), userDto);
  }
}
