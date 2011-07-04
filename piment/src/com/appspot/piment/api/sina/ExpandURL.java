package com.appspot.piment.api.sina;

public class ExpandURL implements java.io.Serializable {

  private static final long serialVersionUID = -6676818887154632470L;
  private String url_short;
  private String url_long;
  private int type;

  public String getUrl_short() {
	return url_short;
  }

  public void setUrl_short(String url_short) {
	this.url_short = url_short;
  }

  public String getUrl_long() {
	return url_long;
  }

  public void setUrl_long(String url_long) {
	this.url_long = url_long;
  }

  public int getType() {
	return type;
  }

  public void setType(int type) {
	this.type = type;
  }

  @Override
  public String toString() {
	return "ExpandURL [url_short=" + url_short + ", url_long=" + url_long + ", type=" + type + "]";
  }

}
