package com.appspot.piment.shared;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

  public static Map<String, String> toParamMap(String str) {

    Map<String, String> result = new HashMap<String, String>();

    String[] params = str.split("[&]");

    for (String param : params) {
      String[] kv = param.split("[=]");
      result.put(kv[0], kv[1]);
    }

    return result;
  }
  
  public static boolean isNotBlank(String str) {
    return (str != null && str.trim().length() > 0) ? true : false;
  }

}
