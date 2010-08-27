package kirin.client;

import kirin.client.model.LoginInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface KirinServiceAsync {
	void loadData(LoginInfo loginInfo, AsyncCallback<String> callback);
}
