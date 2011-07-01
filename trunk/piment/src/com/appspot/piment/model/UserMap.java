package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserMap {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String tqqUserId;

  @Persistent
  private String sinaUserId;

  /** 同期化処理失敗時リトライ制約フラグ */
  @Persistent
  private boolean retryAction;

  @Persistent
  private Date createTime;

  @Persistent
  private String creator;

  @Persistent
  private Date updateTime;

  @Persistent
  private String updator;

  /** 制御フラグ */
  @Persistent
  private boolean disable;

  public UserMap() {
	super();
  }

  public UserMap(String tqqUserId, String sinaUserId) {
	super();
	this.tqqUserId = tqqUserId;
	this.sinaUserId = sinaUserId;
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getTqqUserId() {
	return tqqUserId;
  }

  public void setTqqUserId(String tqqUserId) {
	this.tqqUserId = tqqUserId;
  }

  public String getSinaUserId() {
	return sinaUserId;
  }

  public void setSinaUserId(String sinaUserId) {
	this.sinaUserId = sinaUserId;
  }

  public boolean isRetryAction() {
	return retryAction;
  }

  public void setRetryAction(boolean retryAction) {
	this.retryAction = retryAction;
  }

  public Date getCreateTime() {
	return createTime;
  }

  public void setCreateTime(Date createTime) {
	this.createTime = createTime;
  }

  public String getCreator() {
	return creator;
  }

  public void setCreator(String creator) {
	this.creator = creator;
  }

  public Date getUpdateTime() {
	return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
  }

  public String getUpdator() {
	return updator;
  }

  public void setUpdator(String updator) {
	this.updator = updator;
  }

  public boolean isDisable() {
	return disable;
  }

  public void setDisable(boolean disable) {
	this.disable = disable;
  }

  @Override
  public String toString() {
	return "UserMap [id=" + id + ", tqqUserId=" + tqqUserId + ", sinaUserId=" + sinaUserId + ", retryAction=" + retryAction + ", createTime=" + createTime + ", creator=" + creator + ", updateTime="
	    + updateTime + ", updator=" + updator + ", disable=" + disable + "]";
  }
}
