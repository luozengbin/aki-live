package com.appspot.piment.api.tqq;

public class ResponseData implements java.io.Serializable {

  private static final long serialVersionUID = 4127665371452111629L;

  private String id;
  private String time;

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public String getTime() {
	return time;
  }

  public void setTime(String time) {
	this.time = time;
  }

  @Override
  public String toString() {
	return "ResponseData [id=" + id + ", time=" + time + "]";
  }

}
