package com.davidrodriguez.projecttf.config;

import com.davidrodriguez.projecttf.service.security.SecretProvider;
import com.davidrodriguez.projecttf.service.security.impl.SecretsManagerSecretProvider;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class SecretProviderConfig {


  private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Bean
  SecretProvider secretProvider() {
    SecretProvider secretProvider;
    secretProvider = new SecretsManagerSecretProvider();
    logger.debug("SecretsManagerSecretProvider created");

    return secretProvider;
  }

}
