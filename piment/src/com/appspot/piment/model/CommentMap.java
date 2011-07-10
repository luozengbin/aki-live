package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class CommentMap {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private Long userMapId;

  @Persistent
  private WeiboSource source;

  @Persistent
  private Long wieboId;

  @Persistent
  private Long tqqCommentId;

  @Persistent
  private Long sinaCommentId;

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

  public Long getWieboId() {
	return wieboId;
  }

  public void setWieboId(Long wieboId) {
	this.wieboId = wieboId;
  }

  public Long getTqqCommentId() {
	return tqqCommentId;
  }

  public void setTqqCommentId(Long tqqCommentId) {
	this.tqqCommentId = tqqCommentId;
  }

  public Long getSinaCommentId() {
	return sinaCommentId;
  }

  public void setSinaCommentId(Long sinaCommentId) {
	this.sinaCommentId = sinaCommentId;
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
	return "CommentMap [id=" + id + ", userMapId=" + userMapId + ", source=" + source + ", wieboId=" + wieboId + ", tqqCommentId=" + tqqCommentId + ", sinaCommentId=" + sinaCommentId + ", status="
	    + status + ", retryCount=" + retryCount + ", createTime=" + createTime + ", creator=" + creator + ", updateTime=" + updateTime + ", updator=" + updator + "]";
  }

}
