package gfriends.client.model;

import java.util.Date;

public class ContactItem implements java.io.Serializable {

  private static final long serialVersionUID = 5934518607422959575L;

  private String nickName;
  
  private String email;

  private Date registerDate;

  private boolean enable;

  private String style;

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getNickName() {
    return nickName;
  }

  public void setRegisterDate(Date registerDate) {
    this.registerDate = registerDate;
  }

  public Date getRegisterDate() {
    return registerDate;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getStyle() {
    return style;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

}
