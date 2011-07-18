package com.appspot.piment.api.tqq.model;

public class TimelineResponse extends BaseResponse implements java.io.Serializable {

  private static final long serialVersionUID = 408073353852895713L;

  private TimelineData data;

  public TimelineData getData() {
	return data;
  }

  public void setData(TimelineData data) {
	this.data = data;
  }

  @Override
  public String toString() {
	return "TimelineResponse [data=" + data + ", toString()=" + super.toString() + "]";
  }

}
