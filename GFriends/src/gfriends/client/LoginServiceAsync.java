package gfriends.client;

import gfriends.client.model.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void login(String requestUri, AsyncCallback<LoginInfo> async);
}
