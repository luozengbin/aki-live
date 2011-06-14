package com.appspot.weibotoqq.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AuthService</code>.
 */
public interface AuthServiceAsync {
  
  void requestToken(AsyncCallback<String> callback) throws IllegalArgumentException;

  void exchangeToken(String oauth_token, String oauth_verifier, AsyncCallback<String> callback);
}
