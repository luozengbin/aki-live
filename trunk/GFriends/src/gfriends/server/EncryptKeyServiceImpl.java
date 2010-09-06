package gfriends.server;

import gfriends.client.EncryptKeyService;
import gfriends.server.model.Contact;
import gfriends.server.model.EncryptKey;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EncryptKeyServiceImpl extends RemoteServiceServlet implements EncryptKeyService {

  private static final long serialVersionUID = -2273820896811731653L;

  @SuppressWarnings("unchecked")
  @Override
  public String getKey() {

    PersistenceManager pm = null;

    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    if (user != null) {
      pm = PMF.get().getPersistenceManager();
      String query = "select from " + Contact.class.getName() + " where email == :email";
      List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute(user.getEmail());
      if (contactList != null && contactList.size() > 0 && contactList.get(0).isEnable()) {
        query = "select from " + EncryptKey.class.getName();
        List<EncryptKey> encryptKeyList = (List<EncryptKey>) pm.newQuery(query).execute();
        if (encryptKeyList != null && encryptKeyList.size() > 0) {
          return encryptKeyList.get(0).getContent();
        }
      }
    }
    return "gfriends";
  }

}
