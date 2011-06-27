package com.appspot.piment.api.sina;

import java.util.List;
import java.util.logging.Logger;

import weibo4j.Paging;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());
  
  public WeiboApi() {
	super();
  }

  public WeiboApi(AuthToken authToken) {
	super(authToken);
  }

  public List<Status> getUserTimeline(UserMap userMap) {
	
	try {

	  WeiboMapDao weiboMapDao = new WeiboMapDao();
	  
	  WeiboMap lastestCreateWeiboMap = weiboMapDao.getNewestItem(userMap.getId());

	  Paging paging = new Paging();
	  paging.setPage(1);
	  paging.setCount(20);
	  if (lastestCreateWeiboMap != null) {
		paging.setSinceId(Long.valueOf(lastestCreateWeiboMap.getSinaWeiboId()));
	  }
	  
	  return this.weibo.getUserTimeline(paging);
	  
	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }

}
