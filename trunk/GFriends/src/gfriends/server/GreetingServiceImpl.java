package gfriends.server;

import gfriends.client.GreetingService;
import gfriends.client.model.GreetingItem;
import gfriends.server.model.Greeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

  private static final long serialVersionUID = -4809886475604728588L;

  @Override
  public List<GreetingItem> pushMessage(String content) {

    PersistenceManager pm = null;

    if (content != null && content.trim().length() > 0) {

      // store

      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();

      Date date = new Date();
      Greeting greeting = new Greeting(user, content, date);

      try {
        pm = PMF.get().getPersistenceManager();
        pm.makePersistent(greeting);
      } finally {
        if (pm != null) {
          pm.close();
          pm = null;
        }
      }
    }

    return loadGreeting();
  }

  public List<GreetingItem> loadGreeting() {

    PersistenceManager pm = null;
    List<GreetingItem> result = new ArrayList<GreetingItem>();

    // select
    try {

      pm = PMF.get().getPersistenceManager();

      String query = "select from " + Greeting.class.getName() + " order by date desc range 0,20";

      @SuppressWarnings("unchecked")
      List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute();

      GreetingItem greetingItem = null;
      for (Greeting currentGreeting : greetings) {
        greetingItem = new GreetingItem();
        if (currentGreeting.getAuthor() != null) {
          greetingItem.setNickName(currentGreeting.getAuthor().getNickname());
          greetingItem.setEmail(currentGreeting.getAuthor().getEmail());
        }
        greetingItem.setContent(currentGreeting.getContent());
        greetingItem.setDataTime(currentGreeting.getDate());
        greetingItem.setTimestamp(currentGreeting.getDate().getTime());
        result.add(greetingItem);
      }

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }

    return result;
  }
}
