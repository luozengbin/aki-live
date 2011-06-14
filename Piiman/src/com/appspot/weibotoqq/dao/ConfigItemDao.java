package com.appspot.weibotoqq.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.weibotoqq.model.ConfigItem;

public class ConfigItemDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String query = "select from " + ConfigItem.class.getName() + " where key == :key";

  private PersistenceManager pm = null;

  public ConfigItemDao() {
    super();
  }

  public String getValue(String key) {
    try {
      pm = PMF.get().getPersistenceManager();
      
      @SuppressWarnings("unchecked")
      List<ConfigItem> contactList = (List<ConfigItem>) pm.newQuery(query).execute(key);

      return (contactList != null && contactList.size() > 0) ? contactList.get(0).getValue() : null;

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }
}
