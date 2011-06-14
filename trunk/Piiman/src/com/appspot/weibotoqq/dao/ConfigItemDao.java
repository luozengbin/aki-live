package com.appspot.weibotoqq.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.appspot.weibotoqq.model.ConfigItem;

public class ConfigItemDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + ConfigItem.class.getName() + " where key == :key";
  private static final String QL_002 = "select from " + ConfigItem.class.getName();

  private PersistenceManager pm = null;

  public ConfigItemDao() {
    super();
  }

  public String getValue(String key) {
    try {
      pm = PMF.get().getPersistenceManager();

      @SuppressWarnings("unchecked")
      List<ConfigItem> configItemList = (List<ConfigItem>) pm.newQuery(QL_001).execute(key);

      return (configItemList != null && configItemList.size() > 0) ? configItemList.get(0).getValue() : null;

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }

  public Map<String, String> getValues() {

    Map<String, String> values = new HashMap<String, String>();

    try {
      pm = PMF.get().getPersistenceManager();

      @SuppressWarnings("unchecked")
      List<ConfigItem> configItemList = (List<ConfigItem>) pm.newQuery(QL_002).execute();

      if (configItemList != null) {

        for (ConfigItem configItem : configItemList) {
          values.put(configItem.getKey(), configItem.getValue());
        }

      }

      return values;

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }
}
