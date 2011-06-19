package com.appspot.piment.rpc;

import java.util.logging.Logger;

import com.appspot.piment.Constants;
import com.appspot.piment.client.rpc.TQQAuthService;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.api.tqq.OAuthApi;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
public class TQQAuthServiceImpl extends RemoteServiceServlet implements TQQAuthService {
  
  private static final long serialVersionUID = 1596146959816008191L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + TQQAuthServiceImpl.class.getName());

  private AuthTokenDao authTokenDao = new AuthTokenDao();

  private ConfigItemDao configItemDao = new ConfigItemDao();

  public String requestToken() throws IllegalArgumentException {
    try {
      
      log.fine("-- processing requestToken  --");
      
      OAuthApi oAuth = new OAuthApi();

      AuthToken authToken = oAuth.requestToken();

      this.authTokenDao.save(authToken);

      return configItemDao.getValue("qq.authorize.url") + "?oauth_token=" + authToken.getToken();

    } catch (Exception e) {
      
      throw new RuntimeException(e);
    }
  }

  public String exchangeToken(String oauth_token, String oauth_verifier) throws IllegalArgumentException {
    try {

      log.fine("-- processing exchangeToken  --");
      
      AuthToken authToken = this.authTokenDao.getByToken(oauth_token);

      OAuthApi oAuth = new OAuthApi(authToken);

      authToken = oAuth.exchangeToken(oauth_verifier);
      authTokenDao.save(authToken);

      return authToken.getUserName() + "さんから認可を得ました！";

    } catch (Exception e) {

      throw new RuntimeException(e);
    }
  }

}
