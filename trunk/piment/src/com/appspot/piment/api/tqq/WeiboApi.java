package com.appspot.piment.api.tqq;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.appspot.piment.Constants;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.HttpClient;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());

  public WeiboApi(AuthToken authToken) {
    super(authToken);
  }

  public void sendMessage(String msg, String clientIp) throws Exception {

    Map<String, String> params = new LinkedHashMap<String, String>();

    params.put("clientip", StringUtils.isNotBlank(clientIp) ? clientIp : Constants.LOOPBACK_IP);
    params.put("content", msg);
    params.put("format", "json");
    params.putAll(getFixedParams());

    String url = configMap.get("qq.weibo.send.text.url");

    String postPayload = getSignedPayload(Constants.HTTP_POST, url, params);

    log.info("postPayload = " + postPayload);

    String response = HttpClient.doPost(url, postPayload);

    log.info("result --> \n" + response);

  }
  
  
  public String fetchMessage(String startTime) throws Exception {
    
    log.info("start fetchMessage --> startTime: "+ startTime);
    
    Map<String, String> params = new LinkedHashMap<String, String>();

    params.put("format", "json");
    params.put("lastid", "0");
    params.putAll(getFixedParams());
    params.put("pageflag", "0");
    params.put("pagetime", "0");
    params.put("reqnum", "20");

    String url = configMap.get("qq.weibo.broadcast.timeline.url");

    String final_url = getSignedURL(Constants.HTTP_GET, url, params);
    
    log.info("final_url -->" + final_url);
    
    String response = HttpClient.doGet(final_url);
    
    return response;
  }
}
