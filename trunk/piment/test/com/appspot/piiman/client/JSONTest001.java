package com.appspot.piiman.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

import net.arnx.jsonic.JSON;

import com.appspot.piment.api.tqq.Response;

public class JSONTest001 {

  /**
   * @param args
   * @throws UnsupportedEncodingException 
   */
  public static void main(String[] args) throws UnsupportedEncodingException {
	

	//Response responseObj = JSON.decode("{\"data\":{\"id\":\"36588023850264\",\"time\":1309412587},\"errcode\":0,\"msg\":\"ok\",\"ret\":0}", Response.class);
	
	
	//System.out.println(responseObj);
	
	System.out.println(URLEncoder.encode("毕业床单展又开始了，今年的主题你懂的…… http://t.cn/aCDYp3 ", "utf-8"));
	
	
	
	
  }

}
