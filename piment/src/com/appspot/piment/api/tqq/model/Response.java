package com.appspot.piment.api.tqq.model;

public class Response extends BaseResponse implements java.io.Serializable {

  private static final long serialVersionUID = 3709675295989105677L;

  private ResponseData data;

  public ResponseData getData() {
	return data;
  }

  public void setData(ResponseData data) {
	this.data = data;
  }

  @Override
  public String toString() {
	return "Response [data=" + data + ", toString()=" + super.toString() + "]";
  }
}
