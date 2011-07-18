package com.appspot.piment.api.tqq.model;

import java.util.Arrays;
import java.util.Date;

public class TimelineData implements java.io.Serializable {

  private static final long serialVersionUID = -6459013998294978527L;

  private boolean hasnext;

  private Date timestamp;

  private int totalnum;

  private Info[] info;

  public boolean isHasnext() {
	return hasnext;
  }

  public void setHasnext(boolean hasnext) {
	this.hasnext = hasnext;
  }

  public Date getTimestamp() {
	return timestamp;
  }

  public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
  }

  public int getTotalnum() {
	return totalnum;
  }

  public void setTotalnum(int totalnum) {
	this.totalnum = totalnum;
  }

  public Info[] getInfo() {
	return info;
  }

  public void setInfo(Info[] info) {
	this.info = info;
  }

  @Override
  public String toString() {
	return "TimelineData [hasnext=" + hasnext + ", timestamp=" + timestamp + ", totalnum=" + totalnum + ", info=" + Arrays.toString(info) + "]";
  }

  public class Info implements java.io.Serializable {

	private static final long serialVersionUID = -8274703478073121740L;
	private String id;
	private Date timestamp;

	public String getId() {
	  return id;
	}

	public void setId(String id) {
	  this.id = id;
	}

	public Date getTimestamp() {
	  return timestamp;
	}

	public void setTimestamp(Date timestamp) {
	  this.timestamp = timestamp;
	}

	@Override
	public String toString() {
	  return "Info [id=" + id + ", timestamp=" + timestamp + "]";
	}

  }

}
