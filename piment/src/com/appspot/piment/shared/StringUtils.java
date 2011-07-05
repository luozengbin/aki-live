package com.appspot.piment.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

  private static final String HTTP_URL_PREFIX = "http://";

  private static final String HALF_SPACE = " ";

  private static final String FULL_SPACE = "　";

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

  public static String getFileName(String fileUrl) {
	return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
  }

  public static boolean isNotBlank(String str) {
	return (str != null && str.trim().length() > 0) ? true : false;
  }

  public static boolean isBlank(String str) {
	return (str == null || str.trim().length() == 0) ? true : false;
  }

  public static List<String> getUrlList(String originalMsg) {

	List<String> result = new ArrayList<String>();

	StringBuilder sb = new StringBuilder(originalMsg);

	int idxOfURLPerfix = -1;
	int idxOfSpace = -1;
	while ((idxOfURLPerfix = sb.lastIndexOf(HTTP_URL_PREFIX)) >= 0) {

	  String tempStr = sb.substring(idxOfURLPerfix);
	  idxOfSpace = tempStr.indexOf(HALF_SPACE);
	  if (idxOfSpace < 0) {
		idxOfSpace = tempStr.indexOf(FULL_SPACE);
	  }

	  if (idxOfSpace >= 0) {
		result.add(tempStr.substring(0, idxOfSpace));
	  } else {
		result.add(tempStr);
	  }

	  sb = sb.delete(idxOfURLPerfix, sb.length() - 1);
	}

	return result;
  }

}
