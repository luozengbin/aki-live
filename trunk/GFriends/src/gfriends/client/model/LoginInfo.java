package gfriends.client.model;

import java.io.Serializable;

public class LoginInfo implements Serializable {
	
	private static final long serialVersionUID = -2622768711706835584L;
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	private String tocken;
	private boolean registed;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setTocken(String tocken) {
		this.tocken = tocken;
	}

	public String getTocken() {
		return tocken;
	}

  public void setRegisted(boolean registed) {
    this.registed = registed;
  }

  public boolean isRegisted() {
    return registed;
  }
}
