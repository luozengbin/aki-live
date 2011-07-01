package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class WeiboMap {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private Long userMapId;

  @Persistent
  private WeiboSource source;

  @Persistent
  private String tqqWeiboId;

  @Persistent
  private String sinaWeiboId;

  @Persistent
  private WeiboStatus status;

  @Persistent
  private int retryCount;

  @Persistent
  private Date createTime;

  @Persistent
  private String creator;

  @Persistent
  private Date updateTime;

  @Persistent
  private String updator;

  public WeiboMap() {
	super();
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public Long getUserMapId() {
	return userMapId;
  }

  public void setUserMapId(Long userMapId) {
	this.userMapId = userMapId;
  }

  public WeiboSource getSource() {
	return source;
  }

  public void setSource(WeiboSource source) {
	this.source = source;
  }

  public String getTqqWeiboId() {
	return tqqWeiboId;
  }

  public void setTqqWeiboId(String tqqWeiboId) {
	this.tqqWeiboId = tqqWeiboId;
  }

  public String getSinaWeiboId() {
	return sinaWeiboId;
  }

  public void setSinaWeiboId(String sinaWeiboId) {
	this.sinaWeiboId = sinaWeiboId;
  }

  public WeiboStatus getStatus() {
	return status;
  }

  public void setStatus(WeiboStatus status) {
	this.status = status;
  }

  public int getRetryCount() {
	return retryCount;
  }

  public void setRetryCount(int retryCount) {
	this.retryCount = retryCount;
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
  
  @Override
  public String toString() {
	return "WeiboMap [id=" + id + ", userMapId=" + userMapId + ", source=" + source + ", tqqWeiboId=" + tqqWeiboId + ", sinaWeiboId=" + sinaWeiboId + ", status=" + status + ", retryCount="
	    + retryCount + ", createTime=" + createTime + ", creator=" + creator + ", updateTime=" + updateTime + ", updator=" + updator + "]";
  }

}
