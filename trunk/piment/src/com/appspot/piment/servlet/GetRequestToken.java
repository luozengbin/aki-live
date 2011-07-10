package com.appspot.piment.servlet;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.api.sina.SinaAuthApi;
import com.appspot.piment.api.tqq.TqqAuthApi;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.shared.StringUtils;

public class GetRequestToken extends HttpServlet {

  private static final long serialVersionUID = 1878466801688103871L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + GetRequestToken.class.getName());

  private AuthTokenDao authTokenDao = new AuthTokenDao();

  private ConfigItemDao configItemDao = new ConfigItemDao();

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	try {

	  log.fine("-- processing requestToken  --");

	  String source = req.getParameter("weibo_source");
	  AuthToken authToken = null;
	  String authURL = null;
	  switch (WeiboSource.valueOf(source)) {
	  case Tqq:

		TqqAuthApi tqqOAuth = new TqqAuthApi();
		authToken = tqqOAuth.requestToken();
		authURL = configItemDao.getValue("qq.authorize.url") + "?oauth_token=" + authToken.getToken();
		break;
	  case Sina:
		SinaAuthApi sinaOAuth = new SinaAuthApi();
		authToken = sinaOAuth.requestToken();
		authURL = configItemDao.getValue("sina.authorize.url") + "?oauth_token=" + authToken.getToken();
		break;
	  default:
		break;
	  }

	  if (authToken != null && StringUtils.isNotBlank(authURL)) {
		this.authTokenDao.save(authToken);
		resp.sendRedirect(authURL);
	  }
	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }
}
