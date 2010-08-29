package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("kirin")
public interface KirinService extends RemoteService {
	public List<AlbumModel> loadAlbum(LoginInfo loginInfo);
	public List<PhotoModel> loadPhoto(LoginInfo loginInfo, String albumid);
}
