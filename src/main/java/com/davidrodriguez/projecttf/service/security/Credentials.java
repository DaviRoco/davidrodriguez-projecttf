package com.davidrodriguez.projecttf.service.security;

public class Credentials {
  private String dbname;
  private String engine;
  private String host;
  private String password;
  private String port;
  private String username;

  public String getDbname() {
    return dbname;
  }

  public void setDbname(String dbname) {
    this.dbname = dbname;
  }

  public String getEngine() {
    return engine;
  }

  public void setEngine(String engine) {
    this.engine = engine;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}