package com.appspot.piment.api.tqq.model;

public class BaseResponse implements java.io.Serializable {

  private static final long serialVersionUID = -2437899264178056345L;

  private String errcode;

  private String msg;

  private String ret;

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

  @Override
  public String toString() {
	return "BaseResponse [errcode=" + errcode + ", msg=" + msg + ", ret=" + ret + "]";
  }

  public boolean isOK() {
	boolean result = false;
	if (ResponseStatus.SUCCEED.equals(getRet())) {
	  result = true;
	}
	return result;
  }

}
