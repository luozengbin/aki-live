package com.appspot.piment.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TQQAuthServiceAsync {

  void exchangeToken(String oauth_token, String oauth_verifier, AsyncCallback<String> callback);

  void requestToken(AsyncCallback<String> callback);

}
