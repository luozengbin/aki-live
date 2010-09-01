package gfriends.client;

import gfriends.client.model.GreetingItem;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {

	void pushMessage(String content, AsyncCallback<List<GreetingItem>> callback);

  void loadGreeting(AsyncCallback<List<GreetingItem>> callback);

}
