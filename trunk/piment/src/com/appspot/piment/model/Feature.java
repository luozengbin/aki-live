package com.appspot.piment.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Feature {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key id;

  @Persistent
  private String name;

  @Persistent
  private String value;

  @Persistent
  private UserMap userMap;

  public Feature() {
	super();
  }

  public Feature(com.appspot.piment.model.UserMap userMap, String name, String value) {
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

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public String getValue() {
	return value;
  }

  public void setValue(String value) {
	this.value = value;
  }

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
