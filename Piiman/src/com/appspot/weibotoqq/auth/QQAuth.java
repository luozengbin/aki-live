package com.appspot.weibotoqq.auth;

import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.appspot.weibotoqq.Constants;
import com.appspot.weibotoqq.dao.AuthTokenDao;
import com.appspot.weibotoqq.dao.ConfigItemDao;
import com.appspot.weibotoqq.model.AuthToken;
import com.appspot.weibotoqq.util.HttpClient;

public class QQAuth {

  private static final Logger log = Logger.getLogger(Constants.FQCN + QQAuth.class.getName());

  // HTTP信息处理中使用的编码
  private String http_encoding = null;
  
  //获取未授权的Request Token的URL
  String request_token_url = null;
  
  String access_token_url = null;
  
  //认证成功后浏览器会被重定向到这个url中
  String oauth_callback = null;
  
  // 签名方法，暂只支持HMAC-SHA1
  private String oauth_signature_method = null;

  // App Key(应用信息中的App Key值)
  private String oauth_consumer_key = null;

  // App Secret(应用信息中的App Secret值)
  private String oauth_consumer_secret = null;

  // 版本号，如果有必须为“1.0”
  private String oauth_version = null;
  
  
  //設定値取得用オブジェクト
  private ConfigItemDao configItemDao = null;
  
  private AuthTokenDao authTokenDao = null;
  
  public QQAuth() {
    
    super();
    
    this.configItemDao = new ConfigItemDao();
    
    this.authTokenDao = new AuthTokenDao();
    
    this.http_encoding = configItemDao.getValue("http.encoding");
    
    this.request_token_url =  configItemDao.getValue("qq.request.token.url");
    
    this.access_token_url = configItemDao.getValue("qq.access.token.url");
    
    this.oauth_callback =  configItemDao.getValue("qq.oauth.callback");
    
    this.oauth_signature_method = configItemDao.getValue("qq.oauth.signature.method");
    
    this.oauth_consumer_key = configItemDao.getValue("qq.oauth.consumer.key");
    
    this.oauth_consumer_secret = configItemDao.getValue("qq.oauth.consumer.secret");
    
    this.oauth_version = configItemDao.getValue("qq.oauth.version");
  }
  
  private String getOAuthNonce(){
    
    // 单次值，随机生成的32位字符串，防止重放攻击（每次请求必须不同）
    return UUID.randomUUID().toString().substring(0, 32);
  }
  
  private String getOAuthTimestamp(){
    // 时间戳, 其值是距1970 00:00:00 GMT的秒数，必须是大于0的整数
    return String.valueOf(System.currentTimeMillis()).substring(0, 10);
  }
  
  public String requestToken() throws Exception {
    
    // 请求参数的拼接
    StringBuilder baseUrl = new StringBuilder();
    baseUrl.append("oauth_callback=").append(URLEncoder.encode(oauth_callback, http_encoding));
    baseUrl.append("&oauth_consumer_key=").append(URLEncoder.encode(oauth_consumer_key, http_encoding));
    baseUrl.append("&oauth_nonce=").append(URLEncoder.encode(getOAuthNonce(), http_encoding));
    baseUrl.append("&oauth_signature_method=").append(URLEncoder.encode(oauth_signature_method, http_encoding));
    baseUrl.append("&oauth_timestamp=").append(URLEncoder.encode(getOAuthTimestamp(), http_encoding));
    baseUrl.append("&oauth_version=").append(URLEncoder.encode(oauth_version, http_encoding));

    log.info("baseUrl = " + baseUrl);

    // 签名
    String oauth_signature = signature(Constants.GET_METHOD, request_token_url, baseUrl.toString(), oauth_consumer_secret, null);

    log.info("oauth_signature = " + oauth_signature);

    String request_token_url_final = request_token_url + "?" + baseUrl.toString() + "&oauth_signature=" + URLEncoder.encode(oauth_signature, http_encoding);

    log.info("request_token_url_final = " + request_token_url_final);

    String requestTokenResponse = HttpClient.doGet(request_token_url_final);
    
    log.info("requestTokenResponse ---> " + requestTokenResponse);
    
    String[] responseParams = requestTokenResponse.split("[&]");
    
    log.info("responseParams[0] ---> " + responseParams[0]);
    log.info("responseParams[1] ---> " + responseParams[1]);
    
    String oauth_token = responseParams[0].split("[=]")[1];
    String oauth_token_secret = responseParams[1].split("[=]")[1];
    
    log.info("oauth_token ---> " + oauth_token);
    log.info("oauth_token_secret ---> " + oauth_token_secret);
    
    AuthToken authToken = new AuthToken(Constants.QQ, oauth_token, oauth_token_secret);
    authTokenDao.save(authToken);
    
    return configItemDao.getValue("qq.authorize.url") + "?oauth_token=" + oauth_token;
    
  }
  
  
  public String exchangeToken(String oauth_token, String oauth_verifier) throws Exception {
    
    log.info("exchangeToken = " + oauth_token + ", " + oauth_verifier);
    
    AuthToken authToken = authTokenDao.getByToken(oauth_token);
    
    String oauth_token_secret = authToken.getTokenSecret();

    // 请求参数的拼接
    StringBuilder baseUrl = new StringBuilder();
    baseUrl.append("oauth_consumer_key=").append(URLEncoder.encode(oauth_consumer_key, http_encoding));
    baseUrl.append("&oauth_nonce=").append(URLEncoder.encode(getOAuthNonce(), http_encoding));
    baseUrl.append("&oauth_signature_method=").append(URLEncoder.encode(oauth_signature_method, http_encoding));
    baseUrl.append("&oauth_timestamp=").append(URLEncoder.encode(getOAuthTimestamp(), http_encoding));
    baseUrl.append("&oauth_token=").append(URLEncoder.encode(oauth_token, http_encoding));
    baseUrl.append("&oauth_verifier=").append(URLEncoder.encode(oauth_verifier, http_encoding));
    baseUrl.append("&oauth_version=").append(URLEncoder.encode(oauth_version, http_encoding));

    log.info("baseUrl = " + baseUrl);
    
    // 签名
    String oauth_signature = signature(Constants.GET_METHOD, access_token_url, baseUrl.toString(), oauth_consumer_secret, oauth_token_secret);

    log.info("oauth_signature = " + oauth_signature);

    String access_token_url_final = access_token_url + "?" + baseUrl.toString() + "&oauth_signature=" + URLEncoder.encode(oauth_signature, http_encoding);

    log.info("access_token_url_final = " + access_token_url_final);

    String accessTokenResponse = HttpClient.doGet(access_token_url_final);
    
    log.info("accessTokenResponse ---> " + accessTokenResponse);
    
    String[] responseParams = accessTokenResponse.split("[&]");
    
    log.info("responseParams[0] ---> " + responseParams[0]);
    log.info("responseParams[1] ---> " + responseParams[1]);
    log.info("responseParams[2] ---> " + responseParams[2]);
    
    String new_oauth_token = responseParams[0].split("[=]")[1];
    String new_oauth_token_secret = responseParams[1].split("[=]")[1];
    String user_name = responseParams[2].split("[=]")[1];
    
    log.info("new_oauth_token ---> " + new_oauth_token);
    log.info("new_oauth_token_secret ---> " + new_oauth_token_secret);
    log.info("user_name ---> " + user_name);
    
    authToken.setUserName(user_name);
    authToken.setToken(new_oauth_token);
    authToken.setTokenSecret(new_oauth_token_secret);
    
    authTokenDao.save(authToken);
    
    return accessTokenResponse;
    
  }
  
  private String signature(String method, String url, String urlParams, String consumerSecret, String tokenSecret) throws Exception {

    // 签名对象
    String signatureData = method + "&" + URLEncoder.encode(url, http_encoding) + "&" + URLEncoder.encode(urlParams, http_encoding);
    
    String signatureKey = URLEncoder.encode(consumerSecret, http_encoding) + "&" + (tokenSecret != null ? URLEncoder.encode(tokenSecret, http_encoding): "");
    
    log.info("signatureKey = " + signatureKey);
    log.info("signatureData = " + signatureData);

    // 通过HmacSHA1哈希函数签名
    String byteEncode = "US-ASCII";
    String hashMethod = "HmacSHA1";
    Mac hashMac = Mac.getInstance(hashMethod);
    SecretKeySpec spec = new SecretKeySpec(signatureKey.getBytes(byteEncode), hashMethod);
    hashMac.init(spec);
    byte[] hashByte = hashMac.doFinal((signatureData).getBytes(byteEncode));

    // 签名值
    return new String(Base64.encodeBase64(hashByte));
  }
}
