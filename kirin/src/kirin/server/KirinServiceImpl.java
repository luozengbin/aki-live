package kirin.server;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kirin.client.KirinService;
import kirin.client.model.LoginInfo;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class KirinServiceImpl extends RemoteServiceServlet implements KirinService {

	private static final long serialVersionUID = 9156488597753214690L;
	private static final Log log = LogFactory.getLog(KirinServiceImpl.class);

	@Override
	public String loadData(LoginInfo loginInfo) {
		
		log.info("nihao!!!");
		
		StringBuilder sb = new StringBuilder();
		
		try {
			PicasawebService service = new PicasawebService("Kirin-App");

			URL feedUrl = new URL("http://picasaweb.google.com/data/feed/api/user/" + loginInfo.getNickname() + "?kind=album");

			UserFeed myUserFeed = service.getFeed(feedUrl, UserFeed.class);
			
			for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
				sb.append(myAlbum.getTitle().getPlainText());
			}

		} catch (Exception e) {
			log.error(e);
			sb.append(e.getMessage());
		}
		
		return sb.toString();
	}

}
