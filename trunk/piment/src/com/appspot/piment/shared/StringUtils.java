package com.appspot.piment.shared;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

  public static Map<String, String> toParamMap(String str) {

	Map<String, String> result = new HashMap<String, String>();

	if (str.startsWith("?")) {
	  str = str.substring(1);
	}
	
	String[] params = str.split("[&]");

	for (String param : params) {
	  if (isNotBlank(param) && param.contains("=")) {
		String[] kv = param.split("[=]");
		result.put(kv[0], kv[1]);
	  }
	}

	return result;
  }
  
  public static String getFileName(String fileUrl){
	return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
  }

  public static boolean isNotBlank(String str) {
	return (str != null && str.trim().length() > 0) ? true : false;
  }
  
  public static boolean isBlank(String str) {
	return (str == null || str.trim().length() == 0) ? true : false;
  }

}
