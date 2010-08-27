package kirin.client;


import kirin.client.model.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
