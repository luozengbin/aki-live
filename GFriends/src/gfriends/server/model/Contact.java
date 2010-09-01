package gfriends.server.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Contact {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String nickName;
  
  @Persistent
  private String email;

  @Persistent
  private Date registerDate;
  
  @Persistent
  private boolean enable;
  
  @Persistent
  private String style;
  
  public Contact(String nickName, String email, Date registerDate, boolean enable, String style) {
    super();
    this.nickName = nickName;
    this.email = email;
    this.registerDate = registerDate;
    this.enable = enable;
    this.style = style;
  }

  public Contact() {
    super();
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
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

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getNickName() {
    return nickName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getStyle() {
    return style;
  }
}
