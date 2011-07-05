package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AuthToken {

  public AuthToken() {
	super();
  }

  public AuthToken(WeiboSource type, String token, String tokenSecret) {
	super();
	this.type = type;
	this.token = token;
	this.tokenSecret = tokenSecret;
  }

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private WeiboSource type;

  @Persistent
  private String userName;

  @Persistent
  private String token;

  @Persistent
  private String tokenSecret;

  @Persistent
  private Date createTime;

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public WeiboSource getType() {
	return type;
  }

  public void setType(WeiboSource type) {
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

  public Date getCreateTime() {
	return createTime;
  }

  public void setCreateTime(Date createTime) {
	this.createTime = createTime;
  }

  @Override
  public String toString() {
	return "AuthToken [id=" + id + ", type=" + type + ", userName=" + userName + ", token=" + token + ", tokenSecret=" + tokenSecret + ", createTime=" + createTime + "]";
  }

}
