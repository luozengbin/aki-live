package com.appspot.piment.model;

public class ApiLimitedException extends Exception {
  
  private static final long serialVersionUID = -1961552677339117707L;

  public ApiLimitedException() {
	super();
  }

  public ApiLimitedException(String message, Throwable cause) {
	super(message, cause);
  }

  public ApiLimitedException(String message) {
	super(message);
  }

  public ApiLimitedException(Throwable cause) {
	super(cause);
  }
}
