package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.ItemDto;
import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.entity.User;
import com.davidrodriguez.projecttf.service.UserService;
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

  public UserController(UserService userService) { this.userService = userService; }

  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/user")
  public ResponseEntity<List<UserDto>> get() {
    var listType = new TypeToken<List<UserDto>>() {}.getType();
    var list = (List<UserDto>) modelMapper.map(userService.findAll(), listType);
    return ResponseEntity.ok(list);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserDto> getItemById(@PathVariable Long id) {
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
  public ResponseEntity<User> createItem(@RequestBody UserDto userDto) {
    User createdUser = userService.create(userDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }
  @PutMapping("/user")
  public ResponseEntity<UserDto> updateItem(@RequestBody UserDto userDto) {
    var type = new TypeToken<UserDto>() {}.getType();
    User existingUser = userService.findOne(userDto.getId());
    if (existingUser != null){
      User updatedUser = userService.update(existingUser, userDto);
      UserDto updatedUserDto = modelMapper.map(updatedUser, type);
      return ResponseEntity.ok(updatedUserDto);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/user")
  public ResponseEntity<String> deleteItem(@RequestBody UserDto userDto) {
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
}
