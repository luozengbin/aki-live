package kirin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactModel implements Serializable {
	
	private static final long serialVersionUID = 4747487185097808329L;
	
	private String nikeName;
	
	private String emailAddress;
	
	private List<AlbumModel> albumList = new ArrayList<AlbumModel>();

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setAlbumList(List<AlbumModel> albumList) {
		this.albumList = albumList;
	}

	public List<AlbumModel> getAlbumList() {
		return albumList;
	}

	@Override
	public String toString() {
		return "ContactModel [nikeName=" + nikeName + ", emailAddress=" + emailAddress + ", albumList=" + albumList + "]";
	}
}
