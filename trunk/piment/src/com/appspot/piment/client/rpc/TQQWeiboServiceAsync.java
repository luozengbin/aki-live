package com.appspot.piment.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TQQWeiboServiceAsync {

  void sendMessage(String msg, AsyncCallback<Void> callback);

  void fetchMessage(String startTime, AsyncCallback<String> callback);

}
