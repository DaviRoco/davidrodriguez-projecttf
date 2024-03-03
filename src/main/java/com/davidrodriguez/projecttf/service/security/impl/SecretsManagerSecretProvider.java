package com.davidrodriguez.projecttf.service.security.impl;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.CreateSecretRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.amazonaws.services.secretsmanager.model.UpdateSecretRequest;
import com.davidrodriguez.projecttf.service.security.Credentials;
import com.davidrodriguez.projecttf.service.security.SecretMap;
import com.davidrodriguez.projecttf.service.security.SecretProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.invoke.MethodHandles;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecretsManagerSecretProvider implements SecretProvider {

  private AWSSecretsManager client;
  private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void setClient(AWSSecretsManager client) {
    this.client = client;
  }

  public AWSSecretsManager getClient() {
    if (client != null) {
      return client;
    }
    client = AWSSecretsManagerClientBuilder.standard().build();
    return client;
  }

  @Override
  public Credentials getCredentials(String secretName) {
    var credentials = new Credentials();
    var secretMap = getSecretMap(secretName);
    if (secretMap == null || secretMap.isEmpty()) {
      return credentials;
    }
    credentials.setDbname(secretMap.getOrDefault("dbname", null));
    credentials.setHost(secretMap.getOrDefault("host", null));
    credentials.setPassword(secretMap.getOrDefault("password", null));
    credentials.setPort(secretMap.getOrDefault("port", null));
    credentials.setUsername(secretMap.getOrDefault("username", null));
    return credentials;
  }

  @Override
  public SecretMap getSecretMap(String secretName) {
    var map = retrieveSecretMap(secretName);
    if (map.isPresent()) {
      return map.get();
    }
    throw new ResourceNotFoundException(secretName);
  }

  public Optional<SecretMap> retrieveSecretMap(String secretId) {

    var secretHash = new HashMap<String, String>();
    var secret = getSecretString(secretId);

    if (secret == null || secret.equals("")) {
      return Optional.empty();
    }

    var objectMapper = new ObjectMapper();
    var typeRef = new TypeReference<HashMap<String, String>>() {};

    try {
      secretHash = objectMapper.readValue(secret, typeRef);
    } catch (Exception e) {
      logger.error("Error reading secret", e);
    }

    return Optional.of(new SecretMap(secretId, secretHash));
  }

  @Override
  public boolean updateSecretMap(SecretMap secretMap) {
    var objectMapper = new ObjectMapper();
    getSecretMap(secretMap.getName());
    try {
      var secretString = objectMapper.writeValueAsString(secretMap.getValues());
      var request = new UpdateSecretRequest()
          .withSecretId(secretMap.getName())
          .withSecretString(secretString);
      getClient().updateSecret(request);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public boolean createSecretMap(SecretMap secretMap) {
    var objectMapper = new ObjectMapper();
    var secretId = secretMap.getName();
    String secretString;
    try {
      secretString = objectMapper.writeValueAsString(secretMap.getValues());
      var request = new CreateSecretRequest()
          .withName(secretId)
          .withSecretString(secretString);
      getClient().createSecret(request);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public JSONObject getSecretObject(String secretId) {
    var secret = getSecretString(secretId);
    try {
      return new JSONObject(secret);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  public String getSecretString(String secretId) {
    var getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretId);
    GetSecretValueResult getSecretValueResult;
    try {
      getSecretValueResult = getClient().getSecretValue(getSecretValueRequest);
    } catch (ResourceNotFoundException e) {
      logger.warn(String.format("Secret not found: %s", secretId), e);
      return null;
    } catch (Exception e) {
      logger.error(String.format("Error obtaining secret: %s", secretId), e);
      return null;
    }

    String secret = null;
    if (getSecretValueResult != null) {
      if (getSecretValueResult.getSecretString() != null) {
        secret = getSecretValueResult.getSecretString();
      } else if (getSecretValueResult.getSecretBinary() != null) {
        secret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
      }
    }
    return secret;
  }
}
