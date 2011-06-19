package com.appspot.piment.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("tqq/auth")
public interface TQQAuthService extends RemoteService {
  
  String requestToken() throws IllegalArgumentException;
  
  String exchangeToken(String oauth_token, String oauth_verifier) throws IllegalArgumentException;
}
