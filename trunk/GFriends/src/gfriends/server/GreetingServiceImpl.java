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

		List<GreetingItem> result = new ArrayList<GreetingItem>();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Date date = new Date();
		Greeting greeting = new Greeting(user, content, date);

		// store 
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			pm.makePersistent(greeting);
		} finally {
			pm.close();
			pm = null;
		}
		
		// select
		try {

			pm = PMF.get().getPersistenceManager();

			String query = "select from " + Greeting.class.getName() + " order by date desc range 0,20";

			pm = PMF.get().getPersistenceManager();
			@SuppressWarnings("unchecked")
			List<Greeting> greetings = (List<Greeting>) pm.newQuery(query).execute();

			GreetingItem greetingItem = null;
			for (Greeting currentGreeting : greetings) {
				greetingItem = new GreetingItem();
				if (currentGreeting.getAuthor() != null) {
					greetingItem.setNickName(currentGreeting.getAuthor().getNickname());
				}
				greetingItem.setContent(currentGreeting.getContent());
				greetingItem.setDataTime(currentGreeting.getDate().toString());
				greetingItem.setTimestamp(currentGreeting.getDate().getTime());
				result.add(greetingItem);
			}

		} finally {
			pm.close();
		}

		return result;
	}
}
