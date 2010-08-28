package gdata.album;

import java.io.IOException;
import java.net.URL;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.ServiceException;

public class TestCase001 {
	public static void main(String[] args) throws IOException, ServiceException {
		PicasawebService service = new PicasawebService("Kirin-App");
		service.setUserCredentials("jalen.cn@gmail.com", "akira%milan%");
		
		URL feedUrl = new URL("http://picasaweb.google.com/data/feed/api/user/jalen.cn?kind=album");

		UserFeed userFeed = service.getFeed(feedUrl, UserFeed.class);
		AlbumFeed albumFeed = null;
		for (AlbumEntry albumEntry : userFeed.getAlbumEntries()) {
			System.out.println(albumEntry.getTitle().getPlainText());
			
			albumFeed = albumEntry.getFeed("photo");
			for (PhotoEntry photoEntry : albumFeed.getPhotoEntries()) {
				System.out.println(photoEntry.getTitle().getPlainText());
				System.out.println(photoEntry.getMediaThumbnails().get(2).getUrl());
				System.out.println(photoEntry.getMediaGroup().getContents().get(0).getUrl());
			}
		}
	}
}
