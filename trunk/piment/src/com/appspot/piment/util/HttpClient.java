package com.appspot.piment.util;

import java.io.IOException;

import com.appspot.piment.Constants;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class HttpClient {
  
  public static String doGet(String strUrl) throws IOException {

    URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();

    java.net.URL url = new java.net.URL(strUrl);

    HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET);

    HTTPResponse response = ufs.fetch(request);

    byte[] content = response.getContent();

    return new String(content, Constants.HTTP_ENCODEING);
  }
  
  
  public static String doPost(String strUrl, String payload) throws IOException {

    URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();

    java.net.URL url = new java.net.URL(strUrl);

    HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
    
    request.setPayload(payload.getBytes());

    HTTPResponse response = ufs.fetch(request);

    byte[] content = response.getContent();

    return new String(content, Constants.HTTP_ENCODEING);
  }
}
