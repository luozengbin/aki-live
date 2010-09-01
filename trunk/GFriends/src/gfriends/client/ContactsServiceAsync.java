package gfriends.client;

import gfriends.client.model.ContactItem;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContactsServiceAsync {

  void loadContacts(AsyncCallback<List<ContactItem>> callback);

  void register(String nickname, String email, AsyncCallback<Void> callback);

  void removeAllContact(AsyncCallback<Void> callback);

}
