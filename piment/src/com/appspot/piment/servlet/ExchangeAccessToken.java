package com.appspot.piment.servlet;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.rpc.TQQAuthServiceImpl;

public class ExchangeAccessToken extends HttpServlet {

  private static final long serialVersionUID = 6662180842235417313L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + TQQAuthServiceImpl.class.getName());

  private AuthTokenDao authTokenDao = new AuthTokenDao();

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	try {

	  log.fine("-- processing exchangeToken  --");
	  req.setCharacterEncoding("UTF-8");
	  resp.setContentType("text/html;charset=UTF-8");

	  WeiboSource source = WeiboSource.valueOf(req.getParameter("weibo_source"));
	  String oauth_token = req.getParameter("oauth_token");
	  String oauth_verifier = req.getParameter("oauth_verifier");

	  AuthToken authToken = this.authTokenDao.getByToken(oauth_token);
	  switch (source) {
	  case Sina:
		com.appspot.piment.api.sina.OAuthApi sinaOAuth = new com.appspot.piment.api.sina.OAuthApi(authToken);
		authToken = sinaOAuth.exchangeToken(oauth_verifier);
		break;
	  case Tqq:
		com.appspot.piment.api.tqq.OAuthApi tqqOAuth = new com.appspot.piment.api.tqq.OAuthApi(authToken);
		authToken = tqqOAuth.exchangeToken(oauth_verifier);
		break;
	  default:
		break;
	  }
	  
	  authTokenDao.save(authToken);

	  resp.getWriter().write("<h1>" + authToken.getUserName() + "，您的授权已经通过！</h1>");
	  resp.getWriter().flush();

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }
}
