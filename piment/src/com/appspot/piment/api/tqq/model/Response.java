package com.appspot.piment.api.tqq.model;

public class Response extends BaseResponse implements java.io.Serializable {

	private static final long serialVersionUID = 3709675295989105677L;

	private ResponseData data;

	private String applicationMsg;

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}

	public String getApplicationMsg() {
		return applicationMsg;
	}

	public void setApplicationMsg(String applicationMsg) {
		this.applicationMsg = applicationMsg;
	}

	@Override
	public String toString() {
		return "Response [data=" + data + ", applicationMsg=" + applicationMsg
				+ "]";
	}
}
