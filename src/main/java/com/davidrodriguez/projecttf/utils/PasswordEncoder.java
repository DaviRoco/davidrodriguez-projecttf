package com.davidrodriguez.projecttf.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
  private static final int STRENGTH = 10;

  public static String encodePassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);
    return encoder.encode(password);
  }

  public static boolean checkPassword(String password, String hashedPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(password, hashedPassword);
  }
}
