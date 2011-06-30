package com.appspot.piment.api.tqq;

public class Response implements java.io.Serializable {

  private static final long serialVersionUID = 3709675295989105677L;

  private ResponseData data;

  private String errcode;

  private String msg;

  private String ret;

  public ResponseData getData() {
	return data;
  }

  public void setData(ResponseData data) {
	this.data = data;
  }

  public String getErrcode() {
	return errcode;
  }

  public void setErrcode(String errcode) {
	this.errcode = errcode;
  }

  public String getMsg() {
	return msg;
  }

  public void setMsg(String msg) {
	this.msg = msg;
  }

  public String getRet() {
	return ret;
  }

  public void setRet(String ret) {
	this.ret = ret;
  }

  public static long getSerialversionuid() {
	return serialVersionUID;
  }

  @Override
  public String toString() {
	return "Response [data=" + data + ", errcode=" + errcode + ", msg=" + msg + ", ret=" + ret + "]";
  }

}
