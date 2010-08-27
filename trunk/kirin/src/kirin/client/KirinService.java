package kirin.client;

import kirin.client.model.LoginInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("kirin")
public interface KirinService extends RemoteService {
	public String loadData(LoginInfo loginInfo);
}
