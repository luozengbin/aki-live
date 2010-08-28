package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface KirinServiceAsync {
	void loadData(LoginInfo loginInfo, AsyncCallback<List<AlbumModel>> callback);
}
