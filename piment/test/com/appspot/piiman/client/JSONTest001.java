package com.appspot.piiman.client;

import java.io.UnsupportedEncodingException;

import com.appspot.piment.shared.StringUtils;

public class JSONTest001 {

  /**
   * @param args
   * @throws UnsupportedEncodingException
   */
  public static void main(String[] args) throws UnsupportedEncodingException {

	
	System.out.println(StringUtils.getUrlList("http://www.google.com"));
	
	System.out.println(StringUtils.getUrlList(" http://www.google.com "));
	
	System.out.println(StringUtils.getUrlList(" http://www.google.com http://www.sina.com"));
	
	System.out.println(StringUtils.getUrlList(" http://www.google.com　http://www.sina.com"));
	System.out.println(StringUtils.getUrlList(" http://www.google.com　http://www.sina.com　"));
	
	System.out.println(StringUtils.getUrlList("　　　http://www.google.com　http://www.sina.com　"));
	
	System.out.println(StringUtils.getUrlList("　　　http://www.google.com　/n/n/nhttp://www.sina.com　"));
	
	System.out.println(StringUtils.getUrlList("　　　http://www.google.com,http://www.sina.com　"));
	
  }

}
