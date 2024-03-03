package com.davidrodriguez.projecttf.service.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SecretMap {
  private String name;
  private Map<String, String> values;

  public SecretMap() {
    name = "";
    values = new HashMap<>();
  }

  public SecretMap(String name, Map<String, String> values) {
    this.name = name;
    this.values = values;
  }

  public boolean isEmpty() {
    return values == null || values.isEmpty();
  }

  public String get(String key) {
    return values.get(key);
  }

  public String getOrDefault(String key, String defaultValue) {
    return values.getOrDefault(key, defaultValue);
  }

  public String put(String key, String value) {
    return values.put(key, value);
  }

  public int size() {
    return values.size();
  }

  public boolean containsKey(String key) {
    return values.containsKey(key);
  }

  public  Map<String, String> getValues() { return values;}
  public String getName() {return name;}
}