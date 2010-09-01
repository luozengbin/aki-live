package gfriends.client;

import gfriends.client.model.ContactItem;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contacts")
public interface ContactsService extends RemoteService {
  public List<ContactItem> loadContacts();
  public void register(String nickname, String email);
  public void removeAllContact();
}
