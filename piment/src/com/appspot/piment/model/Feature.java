package com.appspot.piment.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import net.arnx.jsonic.JSONHint;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Feature {

  public enum Name {
	SINA_TO_TQQ, SINA_TO_TQQ_COMMENT, TQQ_TO_SINA, TQQ_TO_SINA_COMMENT, AUTO_RETRY, MESSAGE_VIRIFY
  }

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key id;

  @Persistent
  private Feature.Name name;

  @Persistent
  private String value;

  @Persistent
  private UserMap userMap;

  public Feature() {
	super();
  }

  public Feature(com.appspot.piment.model.UserMap userMap, Feature.Name name, String value) {
	super();
	this.name = name;
	this.value = value;
	this.userMap = userMap;
  }

  public Key getId() {
	return id;
  }

  public void setId(Key id) {
	this.id = id;
  }

  public Feature.Name getName() {
	return name;

  }

  public void setName(Feature.Name name) {
	this.name = name;
  }

  public String getStrName() {
	return name.toString();
  }

  public String getValue() {
	return value;
  }

  public void setValue(String value) {
	this.value = value;
  }

  @JSONHint(ignore = true)
  public UserMap getUserMap() {
	return userMap;
  }

  public void setUserMap(UserMap userMap) {
	this.userMap = userMap;
  }

  @Override
  public String toString() {
	return "Feature [name=" + name + ", value=" + value + "]";
  }

}
