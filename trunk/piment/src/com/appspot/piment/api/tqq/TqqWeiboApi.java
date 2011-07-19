package com.appspot.piment.api.tqq;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;

import com.appspot.piment.Constants;
import com.appspot.piment.api.tqq.model.MessageData;
import com.appspot.piment.api.tqq.model.MessageResponse;
import com.appspot.piment.api.tqq.model.Response;
import com.appspot.piment.api.tqq.model.TimelineData;
import com.appspot.piment.api.tqq.model.TimelineResponse;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.HttpClient;

public class TqqWeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + TqqWeiboApi.class.getName());

  protected String sendTextUrl = null;
  protected String sendVideoUrl = null;
  protected String sendretweetUrl = null;

  protected String sendPicUrl = null;

  public TqqWeiboApi(Map<String, String> configItem) {
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

  public Response sendComment(String retId, String comment, String clientIp) throws Exception {

	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("clientip", StringUtils.isNotBlank(clientIp) ? clientIp : Constants.LOOPBACK_IP);
	params.put("content", comment);
	params.put("format", "json");
	params.putAll(getFixedParams());
	params.put("reid", retId);

	String url = "http://open.t.qq.com/api/t/comment";

	String postPayload = getSignedPayload(Constants.HTTP_POST, url, params);
	log.info("postPayload = " + postPayload);
	String response = HttpClient.doPost(url, postPayload);

	log.info("result --> \n" + response);
	Response responseObj = JSON.decode(response, Response.class);
	return responseObj;
  }

  private Response sendMessage(Map<String, String> params, String pic) throws Exception {

	String url = this.sendTextUrl;

	if (params.containsKey("reid")) {
	  url = this.sendretweetUrl;
	} else {
	  // Videoメッセージ対応
	  String videoUrl = getVideoUrl(params.get("content"));
	  if (StringUtils.isNotBlank(videoUrl)) {
		pic = null; // 画像送信しない
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

  public List<MessageResponse> getUserTimeline(Long sinceId) throws Exception {

	List<MessageResponse> result = new ArrayList<MessageResponse>();

	log.info("start fetchMessage --> sinceId: " + sinceId);
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("format", "json");
	params.putAll(getFixedParams());

	Map<String, String> tempParams = new LinkedHashMap<String, String>();
	if (sinceId != null) {

	  tempParams.put("format", "json");
	  tempParams.put("id", String.valueOf(sinceId));
	  tempParams.putAll(getFixedParams());

	  String single_status_url = "http://open.t.qq.com/api/t/show";
	  String final_single_status_url = getSignedURL(Constants.HTTP_GET, single_status_url, tempParams);
	  log.info("final_single_status_url -->" + final_single_status_url);
	  String singleStatusResponse = HttpClient.doGet(final_single_status_url);

	  log.info("DEBUG --->" + singleStatusResponse);

	  tempParams.clear();

	  MessageResponse msgResponse = JSON.decode(singleStatusResponse, MessageResponse.class);
	  if (msgResponse.isOK()) {
		if (msgResponse.getData() != null) {
		  Date sinceDate = msgResponse.getData().getTimestamp();
		  tempParams.putAll(params);
		  tempParams.put("pageflag", "2");
		  //tempParams.put("reqnum", "0");
		  //tempParams.put("lastid", "0");
		  tempParams.put("pagetime", Long.toString(sinceDate.getTime()));
		} else {
		  //FIXME
		  tempParams.putAll(params);
		  tempParams.put("reqnum", "1");
		}
	  } else {
		throw new RuntimeException(singleStatusResponse);
	  }
	} else {
	  tempParams.putAll(params);
	  tempParams.put("reqnum", this.configMap.get("qq.broadcast.timeline.reqnum"));
	}
	tempParams.put("type", "1");

	String timeline_url = "http://open.t.qq.com/api/statuses/broadcast_timeline_ids";
	String final_timeline_url = getSignedURL(Constants.HTTP_GET, timeline_url, tempParams);
	log.info("final_timeline_url -->" + final_timeline_url);
	String response = HttpClient.doGet(final_timeline_url);
	
	log.info("DEBUG --> " + response);

	TimelineResponse timelineResponse = JSON.decode(response, TimelineResponse.class);

	if (timelineResponse != null && timelineResponse.isOK()) {
	  for (TimelineData.Info timelineInfo : timelineResponse.getData().getInfo()) {
		tempParams.clear();
		tempParams.put("format", "json");
		tempParams.put("id", timelineInfo.getId());
		tempParams.putAll(getFixedParams());

		String url = "http://open.t.qq.com/api/t/show";
		String final_url = getSignedURL(Constants.HTTP_GET, url, tempParams);
		log.info("final_url -->" + final_url);
		String singleStatusResponse = HttpClient.doGet(final_url);
		
		log.info("DEBUG --> " + singleStatusResponse);

		MessageResponse msgResponse = JSON.decode(singleStatusResponse, MessageResponse.class);
		if (msgResponse.isOK()) {
		  result.add(msgResponse);
		} else {
		  MessageData data = new MessageData();
		  data.setId(timelineInfo.getId());
		  data.setTimestamp(timelineInfo.getTimestamp());
		  msgResponse.setData(data);
		  result.add(msgResponse);
		}
	  }
	}

	return result;
  }
}
