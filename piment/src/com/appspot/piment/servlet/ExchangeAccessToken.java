package com.appspot.piment.servlet;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.api.sina.SinaAuthApi;
import com.appspot.piment.api.tqq.TqqAuthApi;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;

public class ExchangeAccessToken extends HttpServlet {

  private static final long serialVersionUID = 6662180842235417313L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + ExchangeAccessToken.class.getName());

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
		SinaAuthApi sinaOAuth = new SinaAuthApi(authToken);
		authToken = sinaOAuth.exchangeToken(oauth_verifier);
		break;
	  case Tqq:
		TqqAuthApi tqqOAuth = new TqqAuthApi(authToken);
		authToken = tqqOAuth.exchangeToken(oauth_verifier);
		break;
	  default:
		break;
	  }

	  authTokenDao.save(authToken);

	  String reloadPage = "<html><head><script type='text/javascript'>if (window.opener && !window.opener.closed) {parent.window.opener.location.reload();}window.close();</script></head></html>";

	  resp.getWriter().write(reloadPage);

	  resp.getWriter().flush();

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }
}
