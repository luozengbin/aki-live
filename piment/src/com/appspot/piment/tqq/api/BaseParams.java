package com.appspot.piment.tqq.api;

public class BaseParams implements java.io.Serializable {

  private static final long serialVersionUID = 2991372711312357886L;

  public BaseParams() {
    super();
  }

  // 签名方法，暂只支持HMAC-SHA1
  private String authSignatureMethod = null;

  // App Key(应用信息中的App Key值)
  private String authConsumerKey = null;

  // App Secret(应用信息中的App Secret值)
  private String authConsumerSecret = null;

  // 版本号，如果有必须为“1.0”
  private String authVersion = null;

  public String getAuthSignatureMethod() {
    return authSignatureMethod;
  }

  public void setAuthSignatureMethod(String authSignatureMethod) {
    this.authSignatureMethod = authSignatureMethod;
  }

  public String getAuthConsumerKey() {
    return authConsumerKey;
  }

  public void setAuthConsumerKey(String authConsumerKey) {
    this.authConsumerKey = authConsumerKey;
  }

  public String getAuthConsumerSecret() {
    return authConsumerSecret;
  }

  public void setAuthConsumerSecret(String authConsumerSecret) {
    this.authConsumerSecret = authConsumerSecret;
  }

  public String getAuthVersion() {
    return authVersion;
  }

  public void setAuthVersion(String authVersion) {
    this.authVersion = authVersion;
  }

  @Override
  public String toString() {
    return "BaseParams [authSignatureMethod=" + authSignatureMethod + ", authConsumerKey=" + authConsumerKey + ", authConsumerSecret=" + authConsumerSecret + ", authVersion=" + authVersion + "]";
  }

}
