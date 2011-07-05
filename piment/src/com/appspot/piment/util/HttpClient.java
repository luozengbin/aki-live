package com.appspot.piment.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import com.appspot.piment.Constants;
import com.appspot.piment.shared.StringUtils;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class HttpClient {

  private static final Logger log = Logger.getLogger(Constants.FQCN + HttpClient.class.getName());

  private static final double TIMEOUT_SECONDS = 10.0;

  public static String doGet(String strUrl) throws IOException {

	URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();

	java.net.URL url = new java.net.URL(strUrl);

	HTTPRequest request = new HTTPRequest(url, HTTPMethod.GET, FetchOptions.Builder.withDeadline(TIMEOUT_SECONDS));

	HTTPResponse response = ufs.fetch(request);

	byte[] content = response.getContent();

	return new String(content, Constants.HTTP_ENCODEING);
  }

  public static String doPost(String strUrl, String payload) throws IOException {

	URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();

	java.net.URL url = new java.net.URL(strUrl);

	HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST, FetchOptions.Builder.withDeadline(TIMEOUT_SECONDS).allowTruncate());

	request.setPayload(payload.getBytes());
	
	log.info("doPost url :" + strUrl);

	HTTPResponse response = ufs.fetch(request);

	byte[] content = response.getContent();

	return new String(content, Constants.HTTP_ENCODEING);
  }

  public static String doPostMultipart(String strUrl, Map<String, String> params, Map<String, String> fileUrlMaps) throws IOException {

	URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();

	java.net.URL url = new java.net.URL(strUrl);

	HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST, FetchOptions.Builder.withDeadline(TIMEOUT_SECONDS).allowTruncate());

	String boundary = makeBoundary();
	request.setHeader(new HTTPHeader("Content-Type", "multipart/form-data; boundary=" + boundary));
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	// form-data
	for (Map.Entry<String, String> formData : params.entrySet()) {
	  write(baos, "--" + boundary + "\r\n");
	  writeFormData(baos, formData.getKey(), formData.getValue());
	}

	// multipart
	for (Map.Entry<String, String> fileUrlMap : fileUrlMaps.entrySet()) {
	  log.info("fetch binary file: " + fileUrlMap.getValue());
	  HTTPRequest donwloadFilerequest = new HTTPRequest(new java.net.URL(fileUrlMap.getValue()), HTTPMethod.GET, FetchOptions.Builder.withDeadline(TIMEOUT_SECONDS));
	  HTTPResponse donwloadFileResponse = ufs.fetch(donwloadFilerequest);

	  byte[] binaryContent = donwloadFileResponse.getContent();

	  String contentType = null;
	  for (HTTPHeader header : donwloadFileResponse.getHeaders()) {
		if (header.getName().toLowerCase().equals("content-type")) {
		  contentType = header.getValue();
		  break;
		}
	  }

	  // file
	  write(baos, "--" + boundary + "\r\n");
	  writeMultipart(baos, fileUrlMap.getKey(), StringUtils.getFileName(fileUrlMap.getValue()), contentType, binaryContent);
	}

	write(baos, "--" + boundary + "--\r\n");

	request.setPayload(baos.toByteArray());

	HTTPResponse response = ufs.fetch(request);

	byte[] content = response.getContent();

	return new String(content, Constants.HTTP_ENCODEING);
  }

  private static void write(OutputStream os, String s) throws IOException {
	os.write(s.getBytes());
  }

  private static void writeFormData(OutputStream os, String name, String value) throws IOException {
	write(os, "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
	os.write(value.getBytes(Constants.HTTP_ENCODEING));
	write(os, "\r\n");
  }

  private static void writeMultipart(OutputStream os, String fieldName, String fileName, String contentType, byte[] bs) throws IOException {
	write(os, "Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"\r\n");
	write(os, "Content-Type: " + contentType + "\r\n\r\n");
	os.write(bs);
	write(os, "\r\n");
  }

  private static String makeBoundary() {

	StringBuilder sb = new StringBuilder();

	Random random = new Random();

	sb.append("---------------------------");
	sb.append(Long.toString(random.nextLong(), 36));
	sb.append(Long.toString(random.nextLong(), 36));
	sb.append(Long.toString(random.nextLong(), 36));
	return sb.toString();
  }
}
