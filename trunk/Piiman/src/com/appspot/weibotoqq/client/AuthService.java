package com.appspot.weibotoqq.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
  
  String requestToken() throws IllegalArgumentException;
  
  String exchangeToken(String oauth_token, String oauth_verifier) throws IllegalArgumentException;
}
