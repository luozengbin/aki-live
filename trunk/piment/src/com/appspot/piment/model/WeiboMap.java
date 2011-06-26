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
  private String tqqWeiboId;

  @Persistent
  private String sinaWeiboId;

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

  public WeiboMap(String tqqWeiboId, String sinaWeiboId) {
	super();
	this.tqqWeiboId = tqqWeiboId;
	this.sinaWeiboId = sinaWeiboId;
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
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
	return "WeiboMap [id=" + id + ", tqqWeiboId=" + tqqWeiboId + ", sinaWeiboId=" + sinaWeiboId + ", createTime=" + createTime + ", creator=" + creator + ", updateTime=" + updateTime + ", updator="
	    + updator + "]";
  }

}
