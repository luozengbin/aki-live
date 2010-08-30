package kirin.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kirin.client.KirinService;
import kirin.client.model.AlbumModel;
import kirin.client.model.ContactModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gdata.client.contacts.ContactQuery;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class KirinServiceImpl extends RemoteServiceServlet implements KirinService {

	private static final long serialVersionUID = 9156488597753214690L;
	private static final Log log = LogFactory.getLog(KirinServiceImpl.class);

	@Override
	public List<ContactModel> loadContact(LoginInfo loginInfo) {

		List<ContactModel> result = new ArrayList<ContactModel>();

		try {
			// myself
			ContactModel contactModel = new ContactModel();
			contactModel.setEmailAddress(loginInfo.getEmailAddress());
			contactModel.setNikeName(loginInfo.getNickname());
			contactModel.setAlbumList(loadAlbum(loginInfo.getNickname()));
			result.add(contactModel);

			// get firends
			ContactsService service = new ContactsService("Kirin-App");
			service.setAuthSubToken(this.getThreadLocalRequest().getSession().getAttribute("sessionToken").toString());

			URL feedUrl = new URL("http://www.google.com/m8/feeds/contacts/default/full");
			ContactQuery contactQuery = new ContactQuery(feedUrl);
			contactQuery.setMaxResults(1000);
			ContactFeed resultFeed = service.getFeed(contactQuery, ContactFeed.class);

			for (ContactEntry contactEntry : resultFeed.getEntries()) {
				if (contactEntry.hasEmailAddresses()) {
					for (Email email : contactEntry.getEmailAddresses()) {
						if (email.getAddress().contains("gmail.com") && !email.getAddress().equals(loginInfo.getEmailAddress())) {
							contactModel = new ContactModel();
							contactModel.setEmailAddress(email.getAddress());
							contactModel.setNikeName(email.getAddress().replace("@gmail.com", ""));

							List<AlbumModel> albumModelList = null;
							try {
								albumModelList = loadAlbum(contactModel.getNikeName());
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (albumModelList != null && albumModelList.size() > 0) {
								contactModel.setAlbumList(albumModelList);
								result.add(contactModel);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}

		return result;
	}

	@Override
	public List<AlbumModel> loadAlbum(String userId) {

		List<AlbumModel> result = new ArrayList<AlbumModel>();

		try {
			PicasawebService service = new PicasawebService("Kirin-App");
			service.setAuthSubToken(this.getThreadLocalRequest().getSession().getAttribute("sessionToken").toString());

			URL feedUrl = new URL("http://picasaweb.google.com/data/feed/api/user/" + userId + "?kind=album");

			UserFeed userFeed = service.getFeed(feedUrl, UserFeed.class);
			AlbumModel albumModel = null;

			for (AlbumEntry albumEntry : userFeed.getAlbumEntries()) {
				albumModel = new AlbumModel();
				albumModel.setAlbumid(albumEntry.getGphotoId());
				albumModel.setName(albumEntry.getTitle().getPlainText());
				albumModel.setUpdate(albumEntry.getUpdated().toString());
				result.add(albumModel);
			}
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	@Override
	public List<PhotoModel> loadPhoto(String userId, String albumid) {
		List<PhotoModel> result = new ArrayList<PhotoModel>();
		try {
			PicasawebService service = new PicasawebService("Kirin-App");
			service.setAuthSubToken(this.getThreadLocalRequest().getSession().getAttribute("sessionToken").toString());
			URL feedUrl = new URL("http://picasaweb.google.com/data/feed/api/user/" + userId + "/albumid/" + albumid);
			AlbumFeed albumFeed = service.getFeed(feedUrl, AlbumFeed.class);
			PhotoModel photoModel = null;
			for (PhotoEntry photoEntry : albumFeed.getPhotoEntries()) {
				photoModel = new PhotoModel();
				photoModel.setTitle(photoEntry.getTitle().getPlainText());
				photoModel.setThumbURL((photoEntry.getMediaThumbnails().get(2).getUrl()));
				photoModel.setURL(photoEntry.getMediaGroup().getContents().get(0).getUrl());
				result.add(photoModel);
			}
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}
}