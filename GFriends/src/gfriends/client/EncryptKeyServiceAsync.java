package gfriends.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncryptKeyServiceAsync {

  void getKey(AsyncCallback<String> callback);

}
