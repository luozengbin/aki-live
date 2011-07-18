package com.appspot.piment.api.tqq.model;

public class Video implements java.io.Serializable {

  private static final long serialVersionUID = -7000186383487470577L;
  private String picurl;
  private String player;
  private String realurl;
  private String shorturl;
  private String title;

  public String getPicurl() {
	return picurl;
  }

  public void setPicurl(String picurl) {
	this.picurl = picurl;
  }

  public String getPlayer() {
	return player;
  }

  public void setPlayer(String player) {
	this.player = player;
  }

  public String getRealurl() {
	return realurl;
  }

  public void setRealurl(String realurl) {
	this.realurl = realurl;
  }

  public String getShorturl() {
	return shorturl;
  }

  public void setShorturl(String shorturl) {
	this.shorturl = shorturl;
  }

  public String getTitle() {
	return title;
  }

  public void setTitle(String title) {
	this.title = title;
  }

  @Override
  public String toString() {
	return "Video [picurl=" + picurl + ", player=" + player + ", realurl=" + realurl + ", shorturl=" + shorturl + ", title=" + title + "]";
  }

}
