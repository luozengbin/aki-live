package com.appspot.piment.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AuthToken {

  public AuthToken() {
    super();
  }

  public AuthToken(String type, String token, String tokenSecret) {
    super();
    this.type = type;
    this.token = token;
    this.tokenSecret = tokenSecret;
  }

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String type;
  
  @Persistent
  private String userName;

  @Persistent
  private String token;

  @Persistent
  private String tokenSecret;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTokenSecret() {
    return tokenSecret;
  }

  public void setTokenSecret(String tokenSecret) {
    this.tokenSecret = tokenSecret;
  }

  @Override
  public String toString() {
    return "AuthToken [id=" + id + ", type=" + type + ", userName=" + userName + ", token=" + token + ", tokenSecret=" + tokenSecret + "]";
  }

}
