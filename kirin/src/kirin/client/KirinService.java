package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.ContactModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("kirin")
public interface KirinService extends RemoteService {
	
	public List<ContactModel> loadContact(LoginInfo loginInfo);
	public List<AlbumModel> loadAlbum(String userId);
	public List<PhotoModel> loadPhoto(String userId, String albumid);
}
