package gfriends.server;

import gfriends.client.ContactsService;
import gfriends.client.model.ContactItem;
import gfriends.server.model.Contact;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        if (contact.isEnable()) {
          contactItem = new ContactItem();
          contactItem.setNickName(contact.getNickName());
          contactItem.setEmail(contact.getEmail());
          contactItem.setRegisterDate(contact.getRegisterDate());
          contactItem.setStyle(contact.getStyle());
          result.add(contactItem);
        }
      }
    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean register(String nickname, String email) {

    PersistenceManager pm = null;
    Contact contact = new Contact(nickname, email, new Date(), true, null);

    try {
      pm = PMF.get().getPersistenceManager();

      String query = "select from " + Contact.class.getName() + " where email == :email";
      List<Contact> contactList = (List<Contact>) pm.newQuery(query).execute(email);

      if (contactList != null && contactList.size() > 0) {
        return false;
      }

      query = "select from " + Contact.class.getName();
      contactList = (List<Contact>) pm.newQuery(query).execute();
      contact.setEnable(false);
      contact.setStyle("contact_" + contactList.size());
      pm.makePersistent(contact);

      sendMail(nickname, email);

      return true;

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }

  private void sendMail(String nickname, String email) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(email, nickname));
      msg.addRecipient(Message.RecipientType.TO, new InternetAddress("jalen.cn@gmail.com", "akira"));
      msg.setSubject("Registration For GFriends");
      msg.setText("Detail:\n nickname : " + nickname 
          + "\n email: " + email
          + "\n https://appengine.google.com/datastore/explorer?&app_id=akirawebapps");
      Transport.send(msg);
    } catch (AddressException e) {
    } catch (MessagingException e) {
    } catch (UnsupportedEncodingException e) {
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
