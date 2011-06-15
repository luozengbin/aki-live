package com.appspot.piment.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tqq/weibo")
public interface TQQWeiboService extends RemoteService {
  
  void sendMessage(String msg);
  String fetchMessage(String startTime);
}
