package com.appspot.weibotoqq.server;

import java.util.logging.Logger;

import com.appspot.weibotoqq.Constants;
import com.appspot.weibotoqq.auth.QQAuth;
import com.appspot.weibotoqq.client.AuthService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {

  private static final Logger log = Logger.getLogger(Constants.FQCN + AuthServiceImpl.class.getName());

  public String requestToken() throws IllegalArgumentException {
    try {

      QQAuth qqAuth = new QQAuth();

      return qqAuth.requestToken();

    } catch (Exception e) {
      
      e.printStackTrace();
      
      throw new IllegalArgumentException("予期しないエラーが起きました！", e);
    }
  }
  
  public String exchangeToken(String oauth_token, String oauth_verifier) throws IllegalArgumentException {
    try {

      QQAuth qqAuth = new QQAuth();

      return qqAuth.exchangeToken(oauth_token, oauth_verifier);

    } catch (Exception e) {
      
      e.printStackTrace();
      
      throw new IllegalArgumentException("予期しないエラーが起きました！", e);
    }
  }

  // // Verify that the input is valid.
  // if (!FieldVerifier.isValidName(input)) {
  // // If the input is not valid, throw an IllegalArgumentException back to
  // // the client.
  // throw new
  // IllegalArgumentException("Name must be at least 4 characters long");
  // }
  //
  // String serverInfo = getServletContext().getServerInfo();
  // String userAgent = getThreadLocalRequest().getHeader("User-Agent");
  //
  // // Escape data from the client to avoid cross-site script vulnerabilities.
  // input = escapeHtml(input);
  // userAgent = escapeHtml(userAgent);
  //
  // return "Hello, " + input + "!<br><br>I am running " + serverInfo +
  // ".<br><br>It looks like you are using:<br>" + userAgent;

  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html
   *          the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }

}
