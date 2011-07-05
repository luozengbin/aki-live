package com.appspot.piment.api.tqq;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;

import com.appspot.piment.Constants;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.HttpClient;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());

  protected String sendTextUrl = null;
  protected String sendVideoUrl = null;
  protected String sendretweetUrl = null;

  protected String sendPicUrl = null;

  public WeiboApi(Map<String, String> configItem) {
	super(configItem);
	this.sendTextUrl = configMap.get("qq.weibo.send.text.url");
	this.sendVideoUrl = configMap.get("qq.weibo.send.video.url");
	this.sendretweetUrl = configMap.get("qq.weibo.send.retweet.url");
	this.sendPicUrl = configMap.get("qq.weibo.send.pic.url");
  }

  public Response sendMessage(String msg, String pic, String clientIp) throws Exception {
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("clientip", StringUtils.isNotBlank(clientIp) ? clientIp : Constants.LOOPBACK_IP);
	params.put("content", msg);
	params.put("format", "json");
	params.putAll(getFixedParams());
	return sendMessage(params, pic);
  }

  public Response retweetMessage(String retweetId, String msg, String pic, String clientIp) throws Exception {
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("clientip", StringUtils.isNotBlank(clientIp) ? clientIp : Constants.LOOPBACK_IP);
	params.put("content", msg);
	params.put("format", "json");
	params.putAll(getFixedParams());
	params.put("reid", retweetId);
	return sendMessage(params, pic);
  }

  private Response sendMessage(Map<String, String> params, String pic) throws Exception {

	String url = this.sendTextUrl;

	if (params.containsKey("reid")) {
	  url = this.sendretweetUrl;
	} else {
	  // Videoメッセージ対応
	  String videoUrl = getVideoUrl(params.get("content"));
	  if (StringUtils.isNotBlank(videoUrl)) {
		pic = null; //画像送信しない
		params.put("url", videoUrl);
		url = this.sendVideoUrl;
	  }
	}

	String response = null;

	if (StringUtils.isNotBlank(pic)) {
	  params.put("oauth_signature", getSignature(Constants.HTTP_POST, this.sendPicUrl, params));
	  Map<String, String> fileUrlMaps = new HashMap<String, String>();
	  fileUrlMaps.put("pic", pic);
	  response = HttpClient.doPostMultipart(this.sendPicUrl, params, fileUrlMaps);
	} else {
	  String postPayload = getSignedPayload(Constants.HTTP_POST, url, params);
	  log.info("postPayload = " + postPayload);
	  response = HttpClient.doPost(url, postPayload);
	}
	log.info("result --> \n" + response);
	Response responseObj = JSON.decode(response, Response.class);
	return responseObj;
  }

  private String getVideoUrl(String msgContect) {

	String url = null;
	List<String> urlList = StringUtils.getUrlList(msgContect);
	String[] videoSites = this.configMap.get("app.piment.video.sites").split(",");

	for (String strUrl : urlList) {
	  for (String videoSite : videoSites) {
		if (strUrl.indexOf(videoSite) >= 0) {
		  url = strUrl;
		  log.info("found video url:" + url);
		  return url;
		}
	  }
	}
	return url;
  }

  public String fetchMessage(String startTime) throws Exception {
	log.info("start fetchMessage --> startTime: " + startTime);
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("format", "json");
	params.put("lastid", "0");
	params.putAll(getFixedParams());
	params.put("pageflag", "0");
	params.put("pagetime", "0");
	params.put("reqnum", "20");
	String url = configMap.get("qq.weibo.broadcast.timeline.url");
	String final_url = getSignedURL(Constants.HTTP_GET, url, params);
	log.info("final_url -->" + final_url);
	String response = HttpClient.doGet(final_url);
	return response;
  }
}
