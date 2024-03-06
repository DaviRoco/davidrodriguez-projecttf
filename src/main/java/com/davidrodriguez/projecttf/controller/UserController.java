package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.service.UserService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
