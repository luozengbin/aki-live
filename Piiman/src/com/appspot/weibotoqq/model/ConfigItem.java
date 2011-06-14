package com.appspot.weibotoqq.model;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ConfigItem {

  public ConfigItem() {
    super();
  }

  public ConfigItem(String key, String value) {
    super();
    this.key = key;
    this.value = value;
  }



  @PrimaryKey
  @Persistent
  private String key;

  @Persistent
  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "ConfigItem [key=" + key + ", value=" + value + "]";
  }
}
