package gfriends.server;

import gfriends.client.ContactsService;
import gfriends.client.model.ContactItem;
import gfriends.server.model.Contact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ContactsServiceImpl extends RemoteServiceServlet implements ContactsService {

  private static final long serialVersionUID = 820454486881334227L;

  @Override
  public List<ContactItem> loadContacts() {

    PersistenceManager pm = null;

    List<ContactItem> result = new ArrayList<ContactItem>();

    try {
      pm = PMF.get().getPersistenceManager();

      String query = "select from " + Contact.class.getName();
      @SuppressWarnings("unchecked")
      List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute();

      ContactItem contactItem = null;
      for (Contact contact : contactList) {
        contactItem = new ContactItem();
        contactItem.setNickName(contact.getNickName());
        contactItem.setEmail(contact.getEmail());
        contactItem.setRegisterDate(contact.getRegisterDate());
        contactItem.setStyle(contact.getStyle());
        result.add(contactItem);
      }

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }

    return result;
  }

  @Override
  public void register(String nickname, String email) {

    PersistenceManager pm = null;
    Contact contact = new Contact(nickname, email, new Date(), true, null);

    try {
      pm = PMF.get().getPersistenceManager();

      String query = "select from " + Contact.class.getName();
      @SuppressWarnings("unchecked")
      List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute();
      contact.setStyle("contact_" + contactList.size());
      pm.makePersistent(contact);
    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }
  
  @Override
  public void removeAllContact() {
    PersistenceManager pm = null;
    try {
      pm = PMF.get().getPersistenceManager();
      String query = "select from " + Contact.class.getName();
      @SuppressWarnings("unchecked")
      List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute();
      pm.deletePersistentAll(contactList);
    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }

}
