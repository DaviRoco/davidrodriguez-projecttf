package com.davidrodriguez.projecttf.service.security.impl;

import com.davidrodriguez.projecttf.service.security.Credentials;
import com.davidrodriguez.projecttf.service.security.SecretMap;
import com.davidrodriguez.projecttf.service.security.SecretProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class EnvironmentSecretProvider implements SecretProvider {

  private final Environment environment;
  private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final Map<String, String> updatedProperties;

  public EnvironmentSecretProvider(Environment environment) {
    this.environment = environment;
    this.updatedProperties = new HashMap<>();
  }

  @Override
  public Credentials getCredentials(String secretName) {
    var secretMap = getSecretMap(secretName);
    var credentials = new Credentials();
    if (secretMap != null) {
      credentials.setDbname(secretMap.get("dbname"));
      credentials.setEngine(secretMap.get("engine"));
      credentials.setHost(secretMap.get("host"));
      credentials.setPassword(secretMap.get("password"));
      credentials.setPort(secretMap.get("port"));
      credentials.setUsername(secretMap.get("username"));
    }
    return credentials;

  }

  @Override
  public SecretMap getSecretMap(String secretName) {
    var secret = getSecretString(secretName);
    if (secret == null) {
      return null;
    }

    HashMap<String, String> values = null;
    try {
      var objectMapper = new ObjectMapper();
      var typeRef = new TypeReference<HashMap<String, String>>() {};
      values = objectMapper.readValue(secret, typeRef);
    } catch (IOException e) {
      logger.warn("Invalid hash value", e);
    }
    return new SecretMap(secretName, values);
  }


  @Override
  public boolean updateSecretMap(SecretMap secretMap) {
    try {
      var objectMapper = new ObjectMapper();
      var secretString = objectMapper.writeValueAsString(secretMap.getValues());
      updatedProperties.put(secretMap.getName(), secretString);
    } catch (JsonProcessingException e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean createSecretMap(SecretMap secretMap) {
    return updateSecretMap(secretMap);
  }

  @Override
  public JSONObject getSecretObject(String secretName) {
    try {
      return new JSONObject(getSecretString(secretName));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  private String getSecretString(String secretName) {
    String secret = null;

    if (updatedProperties.containsKey(secretName)) {
      secret = updatedProperties.get(secretName);
    } else if (environment.containsProperty(secretName)) {
      secret = environment.getProperty(secretName);
    }
    return secret;
  }
}
