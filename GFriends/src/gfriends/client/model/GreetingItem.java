package gfriends.client.model;

import java.io.Serializable;
import java.util.Date;

public class GreetingItem implements Serializable {
  
  private static final long serialVersionUID = 7503692890727680691L;

  private String email;
	
	private String nickName;
	
	private String content;
	
	private Date dataTime;
	
	private long timestamp;
	
	private boolean encrypt;

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setDataTime(Date dataTime) {
    this.dataTime = dataTime;
  }

  public Date getDataTime() {
    return dataTime;
  }

  public void setEncrypt(boolean encrypt) {
    this.encrypt = encrypt;
  }

  public boolean isEncrypt() {
    return encrypt;
  }

}
