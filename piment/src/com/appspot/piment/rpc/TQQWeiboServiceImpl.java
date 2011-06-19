package com.appspot.piment.rpc;

import com.appspot.piment.client.rpc.TQQWeiboService;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.api.tqq.WeiboApi;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TQQWeiboServiceImpl extends RemoteServiceServlet implements TQQWeiboService {

  @Override
  public void sendMessage(String msg) {
    AuthTokenDao authTokenDao = new AuthTokenDao();
    AuthToken authToken = authTokenDao.getByToken("bfe2382e72eb4614890de3f7ae4a19f3");
    WeiboApi weiboApi = new WeiboApi(authToken);
    try {
      weiboApi.sendMessage(msg, getThreadLocalRequest().getRemoteAddr());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String fetchMessage(String startTime) {
    AuthTokenDao authTokenDao = new AuthTokenDao();
    AuthToken authToken = authTokenDao.getByToken("bfe2382e72eb4614890de3f7ae4a19f3");
    WeiboApi weiboApi = new WeiboApi(authToken);
    try {
      return weiboApi.fetchMessage(startTime);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
