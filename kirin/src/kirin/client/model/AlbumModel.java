package kirin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumModel implements Serializable {

	private static final long serialVersionUID = -3506351850303610698L;

	private String albumid;
	
	private String name;

	private String update;
	
	private List<PhotoModel> photos = new ArrayList<PhotoModel>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getUpdate() {
		return update;
	}

	public void setPhotos(List<PhotoModel> photos) {
		this.photos = photos;
	}

	public List<PhotoModel> getPhotos() {
		return photos;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	public String getAlbumid() {
		return albumid;
	}

	@Override
	public String toString() {
		return "AlbumModel [albumid=" + albumid + ", name=" + name + ", update=" + update + "]";
	}
	
}
