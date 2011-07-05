package com.appspot.piment.api.sina;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;
import weibo4j.Paging;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.HttpClient;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());

  public WeiboApi() {
	super();
	this.subInit();
  }

  public WeiboApi(Map<String, String> configItem) {
	super(configItem);
	this.subInit();
  }

  protected void subInit() {
  }

  /**
   * sinaのUserTimelineインタフェースへ問い合わせて、ユーザ発表したメッセージを取得する
   * 
   * @param sinceId
   *          取得条件開始位置のメッセージ
   * @param sinceId
   *          取得条件終了位置のメッセージ
   * 
   * @return メッセージリスト
   */
  public List<Status> getUserTimeline(Long sinceId, Long maxId) {

	try {
	  // sinaへの問い合わせパラメータを初期化する
	  Paging paging = new Paging();
	  // 取得ページ数
	  paging.setPage(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.page")));
	  // 取得数
	  paging.setCount(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.count")));

	  // 取得範囲の開始位置の指定
	  if (sinceId != null) {
		paging.setSinceId(Long.valueOf(sinceId));
	  }

	  // 取得範囲の終了位置の指定
	  if (maxId != null) {
		paging.setMaxId(Long.valueOf(maxId));
	  }

	  log.info("sinaからメッセージIDが \"" + paging.getSinceId() + "\"以降のユーザメッセージを取得する");

	  // 問い合わせを行う
	  return this.weibo.getUserTimeline(paging);

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }

  public static String getStatusPageURL(Long userId, Long statusId) {
	return "http://api.t.sina.com.cn/" + String.valueOf(userId) + "/statuses/" + String.valueOf(statusId);
  }

  public String getOriginalMsg(String sinaMsg) {
	List<String> urls = StringUtils.getUrlList(sinaMsg);
	for (String url : urls) {
	  sinaMsg = sinaMsg.replace(url, getLongUrl(url));
	}
	return sinaMsg;
  }

  public String getLongUrl(String shortUrl) {
	String result = null;
	log.info("ShortUrl : " + shortUrl);
	StringBuilder url = new StringBuilder();
	try {
	  url.append("http://api.t.sina.com.cn/short_url/expand.json?");
	  url.append("source=").append(this.configMap.get("sina.oauth.consumer.key"));
	  url.append("&url_short=").append(shortUrl);
	  String response = HttpClient.doGet(url.toString());
	  ExpandURL expandURL = (JSON.decode(response, ExpandURL[].class))[0];
	  result = expandURL.getUrl_long() != null ? expandURL.getUrl_long() : shortUrl;
	} catch (Exception e) {
	  result = shortUrl;
	}
	log.info("LongUrl : " + result);
	return result;
  }

}
