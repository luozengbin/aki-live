package com.appspot.piment.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.ConfigItem;
import com.appspot.piment.model.Feature;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.util.DateUtils;

public class InitDataDao {

  private PersistenceManager pm = null;

  public InitDataDao() {
	super();
  }

  public void initConfigItem() {
	Map<String, String> values = new LinkedHashMap<String, String>();

	try {
	  pm = PMF.get().getPersistenceManager();

	  values.put("qq.oauth.consumer.key", "");
	  values.put("qq.oauth.consumer.secret", "");
	  values.put("qq.oauth.version", "1.0");
	  values.put("qq.oauth.signature.method", "HMAC-SHA1");
	  values.put("qq.authorize.url", "http://open.t.qq.com/oauth_html/login.php");
	  values.put("qq.oauth.callback", "http://weibotoqq.appspot.com");
	  values.put("qq.request.token.url", "https://open.t.qq.com/cgi-bin/request_token");
	  values.put("qq.access.token.url", "https://open.t.qq.com/cgi-bin/access_token");
	  values.put("qq.weibo.broadcast.timeline.url", "http://open.t.qq.com/api/statuses/broadcast_timeline");
	  values.put("qq.weibo.send.text.url", "http://open.t.qq.com/api/t/add");
	  values.put("qq.weibo.send.pic.url", "http://open.t.qq.com/api/t/add_pic");
	  values.put("qq.weibo.broadcast.timeline.reqnum", "20");
	  values.put("qq.weibo.broadcast.timeline.pageflag ", "0");
	  values.put("qq.weibo.broadcast.timeline.pageflag", "0");
	  values.put("sina.oauth.consumer.key", "");
	  values.put("sina.oauth.consumer.secret", "");
	  values.put("sina.authorize.url", "http://api.t.sina.com.cn/oauth/authenticate");
	  values.put("sina.oauth.callback", "http://weibotoqq.appspot.com");
	  values.put("sina.usertimeline.paging.page", "1");
	  values.put("sina.usertimeline.paging.count", "20");
	  values.put("app.admin.email.displayname", "GAEMonitor");
	  values.put("app.admin.email.address", "");
	  values.put("app.sync.message.max.retry", "3");
	  
	  for (Map.Entry<String, String> entry : values.entrySet()) {
		ConfigItem configItem = new ConfigItem();
		configItem.setKey(entry.getKey());
		configItem.setValue(entry.getValue());
		pm.makePersistent(configItem);
	  }

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public void initUserMap() {
	UserMap userMap = new UserMap();
	userMap.setSinaUserId("");
	userMap.setTqqUserId("");
	userMap.setDisable(false);
	userMap.setFrequency(15);
	userMap.setRetryAction(true);
	userMap.getFeatures().add(new Feature(userMap, "SINA_TO_TQQ", "true"));
	userMap.getFeatures().add(new Feature(userMap, "SINA_TO_TQQ.COMMENT", "true"));
	userMap.getFeatures().add(new Feature(userMap, "TQQ_O_SINA", "true"));
	userMap.getFeatures().add(new Feature(userMap, "TQQ_O_SINA.COMMENT", "true"));
	userMap.setCreateTime(DateUtils.getSysDate());
	userMap.setCreator(UserMapDao.class.getName());
	userMap.setUpdateTime(DateUtils.getSysDate());
	userMap.setUpdator(UserMapDao.class.getName());
	PMF.saveEntity(userMap);
  }

  public void initAuthToken() {
	AuthToken authToken_1 = new AuthToken();
	authToken_1.setToken("");
	authToken_1.setTokenSecret("");
	authToken_1.setType("tqq");
	authToken_1.setUserName("");

	AuthToken authToken_2 = new AuthToken();
	authToken_2.setToken("");
	authToken_2.setTokenSecret("");
	authToken_2.setType("sina");
	authToken_2.setUserName("");

	PMF.saveEntity(authToken_1, authToken_2);

  }
}
