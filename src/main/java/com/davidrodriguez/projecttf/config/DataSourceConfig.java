package com.davidrodriguez.projecttf.config;

import com.davidrodriguez.projecttf.service.security.Credentials;
import com.davidrodriguez.projecttf.service.security.SecretProvider;
import com.davidrodriguez.projecttf.service.security.impl.SecretsManagerSecretProvider;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  public static final String DATASOURCE_CREDENTIALS = "davidrodriguez.projecttf.datasource";
  private final SecretProvider secretProvider;

  public DataSourceConfig(){
    this.secretProvider = new SecretsManagerSecretProvider();
  }
  @Bean
  public DataSource dataSource() {
    var credentials = getCredentials();
    var dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.url(getUrl(getCredentials()));
    dataSourceBuilder.username(credentials.getUsername());
    dataSourceBuilder.password(credentials.getPassword());
    return dataSourceBuilder.build();
  }

  public Credentials getCredentials() {
    return secretProvider.getCredentials(DATASOURCE_CREDENTIALS);
  }

  public String getUrl(Credentials credentials) {
    String url;
    url = "jdbc:mariadb://"
        + credentials.getHost()
        + ":" + credentials.getPort()
        + "/" + credentials.getDbname();
    return url;
  }
}