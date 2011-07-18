package com.appspot.piment.api.tqq.model;

public class Music implements java.io.Serializable {

  private static final long serialVersionUID = 8242177036657178723L;

  private String author;

  private String url;

  private String title;

  public String getAuthor() {
	return author;
  }

  public void setAuthor(String author) {
	this.author = author;
  }

  public String getUrl() {
	return url;
  }

  public void setUrl(String url) {
	this.url = url;
  }

  public String getTitle() {
	return title;
  }

  public void setTitle(String title) {
	this.title = title;
  }

  @Override
  public String toString() {
	return "Music [author=" + author + ", url=" + url + ", title=" + title + "]";
  }

}
