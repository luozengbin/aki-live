package com.appspot.piment.api.tqq;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.model.AuthToken;

public class ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + ApiBase.class.getName());

  protected BaseParams baseParams = null;

  protected AuthToken authToken = null;

  protected Map<String, String> configMap = null;

  public ApiBase() {
	super();
	this.init();
  }

  public ApiBase(AuthToken authToken) {
	super();
	this.init();
	this.authToken = authToken;
  }

  private void init() {

	ConfigItemDao configItemDao = new ConfigItemDao();

	this.configMap = configItemDao.getValues();

	this.baseParams = new BaseParams();

	this.baseParams.setAuthConsumerKey(configMap.get("qq.oauth.consumer.key"));

	this.baseParams.setAuthConsumerSecret(configMap.get("qq.oauth.consumer.secret"));

	this.baseParams.setAuthSignatureMethod(configMap.get("qq.oauth.signature.method"));

	this.baseParams.setAuthVersion(configMap.get("qq.oauth.version"));
  }

  public String getSignedURL(String httpMethod, String targetURL, Map<String, String> params) throws Exception {
	String urlParams = getURLParams(params);
	// 签名
	String oauth_signature = signature(httpMethod, targetURL, urlParams.toString(), baseParams.getAuthConsumerSecret(), authToken != null ? authToken.getTokenSecret() : null);
	log.info("oauth_signature = " + oauth_signature);
	String signedURL = targetURL + "?" + urlParams.toString() + "&oauth_signature=" + encode(oauth_signature);
	return signedURL;
  }

  public String getSignedPayload(String httpMethod, String targetURL, Map<String, String> params) throws Exception {

	String urlParams = getURLParams(params);
	// 签名
	String oauth_signature = signature(httpMethod, targetURL, urlParams.toString(), baseParams.getAuthConsumerSecret(), authToken != null ? authToken.getTokenSecret() : null);
	log.info("oauth_signature = " + oauth_signature);
	String payload = urlParams.toString() + "&oauth_signature=" + encode(oauth_signature);
	return payload;
  }
  
  public String getSignature(String httpMethod, String targetURL, Map<String, String> params) throws Exception {

	String urlParams = getURLParams(params);
	// 签名
	String oauth_signature = signature(httpMethod, targetURL, urlParams.toString(), baseParams.getAuthConsumerSecret(), authToken != null ? authToken.getTokenSecret() : null);
	log.info("oauth_signature = " + oauth_signature);
	return oauth_signature;
  }

  public String getURLParams(Map<String, String> params) throws UnsupportedEncodingException {
	StringBuilder urlParams = new StringBuilder();

	for (Map.Entry<String, String> entry : params.entrySet()) {
	  urlParams.append(entry.getKey()).append("=").append(encode(entry.getValue())).append("&");
	  log.info(entry.getKey() + " = " + entry.getValue());
	}
	urlParams = urlParams.delete(urlParams.length() - 1, urlParams.length());
	return urlParams.toString();
  }

  private String signature(String method, String url, String urlParams, String consumerSecret, String tokenSecret) throws Exception {

	// 签名对象
	String signatureData = method + "&" + encode(url) + "&" + encode(urlParams);

	String signatureKey = encode(consumerSecret) + "&" + (tokenSecret != null ? encode(tokenSecret) : "");

	log.info("signatureKey = " + signatureKey);
	log.info("signatureData = " + signatureData);

	// 通过HmacSHA1哈希函数签名
	String hashAlgorithm = Constants.HMACSHA1_ALGORITHM;
	String byteEncode = Constants.ENCODEING_US_ASCII;
	Mac hashMac = Mac.getInstance(hashAlgorithm);
	SecretKeySpec spec = new SecretKeySpec(signatureKey.getBytes(byteEncode), hashAlgorithm);
	hashMac.init(spec);
	byte[] hashByte = hashMac.doFinal((signatureData).getBytes(byteEncode));

	// 签名值
	return new String(Base64.encodeBase64(hashByte));
  }

  public String getOAuthNonce() {

	// 单次值，随机生成的32位字符串，防止重放攻击（每次请求必须不同）
	return UUID.randomUUID().toString().substring(0, 32);
  }

  public String getOAuthTimestamp() {
	// 时间戳, 其值是距1970 00:00:00 GMT的秒数，必须是大于0的整数
	return String.valueOf(System.currentTimeMillis()).substring(0, 10);
  }

  public Map<String, String> getFixedParams() {

	Map<String, String> fixedParams = new LinkedHashMap<String, String>();
	fixedParams.put("oauth_consumer_key", baseParams.getAuthConsumerKey());
	fixedParams.put("oauth_nonce", getOAuthNonce());
	fixedParams.put("oauth_signature_method", baseParams.getAuthSignatureMethod());
	fixedParams.put("oauth_timestamp", getOAuthTimestamp());
	fixedParams.put("oauth_token", authToken.getToken());
	fixedParams.put("oauth_version", baseParams.getAuthVersion());

	return fixedParams;
  }

  public static String encode(String value) throws UnsupportedEncodingException {
	String encoded = null;
	encoded = URLEncoder.encode(value, Constants.HTTP_ENCODEING);
	StringBuffer buf = new StringBuffer(encoded.length());
	char focus;
	for (int i = 0; i < encoded.length(); i++) {
	  focus = encoded.charAt(i);
	  if (focus == '*') {
		buf.append("%2A");
	  } else if (focus == '+') {
		buf.append("%20");
	  } else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
		buf.append('~');
		i += 2;
	  } else {
		buf.append(focus);
	  }
	}
	return buf.toString();
  }

}
