package com.davidrodriguez.projecttf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;

  private String firstName;

  private String lastNames;

  private String email;

  private String phone;

  private String password;

  private int age;
}
