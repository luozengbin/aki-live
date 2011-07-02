package com.appspot.piment.api.tqq;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.appspot.piment.Constants;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.HttpClient;

public class OAuthApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + OAuthApi.class.getName());

  // 获取未授权的Request Token的URL
  String requestTokenUrl = null;

  String accessTokenUrl = null;

  // 认证成功后浏览器会被重定向到这个url中
  String authCallbackUrl = null;

  public OAuthApi() {
	super();
	subInit();
  }

  public OAuthApi(AuthToken authToken) {
	super(authToken);
	subInit();
  }

  protected void subInit() {
	this.requestTokenUrl = configMap.get("qq.request.token.url");
	this.accessTokenUrl = configMap.get("qq.access.token.url");
	this.authCallbackUrl = configMap.get("qq.oauth.callback");
  }

  public AuthToken requestToken() throws Exception {

	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("oauth_callback", this.authCallbackUrl);
	params.put("oauth_consumer_key", baseParams.getAuthConsumerKey());
	params.put("oauth_nonce", getOAuthNonce());
	params.put("oauth_signature_method", baseParams.getAuthSignatureMethod());
	params.put("oauth_timestamp", getOAuthTimestamp());
	params.put("oauth_version", baseParams.getAuthVersion());

	String request_token_url_final = getSignedURL(Constants.HTTP_GET, this.requestTokenUrl, params);

	log.info("request_token_url_final = " + request_token_url_final);

	String requestTokenResponse = HttpClient.doGet(request_token_url_final);

	log.info("requestTokenResponse ---> " + requestTokenResponse);

	Map<String, String> responseParams = StringUtils.toParamMap(requestTokenResponse);

	String oauth_token = responseParams.get("oauth_token");
	String oauth_token_secret = responseParams.get("oauth_token_secret");

	log.info("oauth_token ---> " + oauth_token);
	log.info("oauth_token_secret ---> " + oauth_token_secret);

	AuthToken authToken = new AuthToken(WeiboSource.Tqq, oauth_token, oauth_token_secret);

	return authToken;
  }

  public AuthToken exchangeToken(String oauth_verifier) throws Exception {

	Map<String, String> params = new LinkedHashMap<String, String>();

	params.put("oauth_consumer_key", baseParams.getAuthConsumerKey());
	params.put("oauth_nonce", getOAuthNonce());
	params.put("oauth_signature_method", baseParams.getAuthSignatureMethod());
	params.put("oauth_timestamp", getOAuthTimestamp());
	params.put("oauth_token", authToken.getToken());
	params.put("oauth_verifier", oauth_verifier);
	params.put("oauth_version", baseParams.getAuthVersion());

	// 签名
	String access_token_url_final = getSignedURL(Constants.HTTP_GET, this.accessTokenUrl, params);

	log.info("access_token_url_final = " + access_token_url_final);

	String accessTokenResponse = HttpClient.doGet(access_token_url_final);

	log.info("accessTokenResponse ---> " + accessTokenResponse);

	Map<String, String> responseParams = StringUtils.toParamMap(accessTokenResponse);

	String new_oauth_token = responseParams.get("oauth_token");
	String new_oauth_token_secret = responseParams.get("oauth_token_secret");
	String user_name = responseParams.get("name");

	log.info("new_oauth_token ---> " + new_oauth_token);
	log.info("new_oauth_token_secret ---> " + new_oauth_token_secret);
	log.info("user_name ---> " + user_name);

	authToken.setUserName(user_name);
	authToken.setToken(new_oauth_token);
	authToken.setTokenSecret(new_oauth_token_secret);

	return authToken;

  }
}
