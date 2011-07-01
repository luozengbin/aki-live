package com.appspot.piment.api.sina;

import java.util.Map;

import weibo4j.Weibo;

import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.model.AuthToken;

public abstract class ApiBase {

  protected Weibo weibo = null;

  protected AuthToken authToken = null;

  protected Map<String, String> configMap = null;

  public ApiBase() {
	super();
	this.init();
  }

  public ApiBase(AuthToken authToken) {
	super();
	this.init();
	this.authToken = authToken;
	this.weibo = new Weibo();
	this.weibo.setOAuthAccessToken(this.authToken.getToken(), this.authToken.getTokenSecret());
  }

  private void init() {
	ConfigItemDao configItemDao = new ConfigItemDao();
	this.configMap = configItemDao.getValues();
	Weibo.CONSUMER_KEY = configMap.get("sina.oauth.consumer.key");
	Weibo.CONSUMER_SECRET = configMap.get("sina.oauth.consumer.secret");
	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
  }

}
