package gfriends.client;

import gfriends.client.model.GreetingItem;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greeting")
public interface GreetingService extends RemoteService {
	public List<GreetingItem> pushMessage(String content);
}
