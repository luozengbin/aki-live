package gfriends.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encryptKey")
public interface EncryptKeyService extends RemoteService {
  public String getKey();
}
