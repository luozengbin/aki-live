package com.appspot.piment.api.tqq.model;

public class MessageResponse extends BaseResponse implements java.io.Serializable {

  private static final long serialVersionUID = -7367833426444216906L;

  private MessageData data;

  public MessageData getData() {
	return data;
  }

  public void setData(MessageData data) {
	this.data = data;
  }

  @Override
  public String toString() {
	return "MessageResponse [data=" + data + ", toString()=" + super.toString() + "]";
  }

}
