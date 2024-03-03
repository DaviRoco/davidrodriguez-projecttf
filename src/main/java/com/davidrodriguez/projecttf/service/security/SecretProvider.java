package com.davidrodriguez.projecttf.service.security;

import org.json.JSONObject;

public interface SecretProvider {
  Credentials getCredentials(String secretName);
  SecretMap getSecretMap(String secretName);
  boolean updateSecretMap(SecretMap secretMap);
  boolean createSecretMap(SecretMap secretMap);
  JSONObject getSecretObject(String secretName);
}
