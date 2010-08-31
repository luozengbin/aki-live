package kirin.client.model;

import java.io.Serializable;

public class PhotoModel implements Serializable{
	
	private static final long serialVersionUID = -7756655304867801201L;

	private String title;
	
	private String thumbURL;
	
	private String URL;
	
	private int height;
	
	private int width;
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setThumbURL(String thumbURL) {
		this.thumbURL = thumbURL;
	}

	public String getThumbURL() {
		return thumbURL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getURL() {
		return URL;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public String toString() {
		return "PhotoModel [title=" + title + ", thumbURL=" + thumbURL + ", URL=" + URL + ", height=" + height + ", width=" + width + "]";
	}
	
}
