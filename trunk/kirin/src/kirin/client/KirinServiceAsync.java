package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface KirinServiceAsync {
	
	void loadAlbum(LoginInfo loginInfo, AsyncCallback<List<AlbumModel>> callback);

	void loadPhoto(LoginInfo loginInfo, String albumid, AsyncCallback<List<PhotoModel>> callback);

}
