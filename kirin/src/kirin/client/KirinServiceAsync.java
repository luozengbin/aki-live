package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.ContactModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface KirinServiceAsync {
	
	void loadContact(LoginInfo loginInfo, AsyncCallback<List<ContactModel>> callback);
	
	void loadAlbum(String userId, AsyncCallback<List<AlbumModel>> callback);

	void loadPhoto(String userId, String albumid, AsyncCallback<List<PhotoModel>> callback);
	
}
