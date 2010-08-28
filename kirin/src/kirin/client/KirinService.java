package kirin.client;

import java.util.List;

import kirin.client.model.AlbumModel;
import kirin.client.model.LoginInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("kirin")
public interface KirinService extends RemoteService {
	public List<AlbumModel> loadData(LoginInfo loginInfo);
}
