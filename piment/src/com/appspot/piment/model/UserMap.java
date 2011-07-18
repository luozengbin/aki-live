package com.appspot.piment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class UserMap {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String tqqUserId;

  @Persistent
  private String sinaUserId;

  /** 同期化頻度 */
  @Persistent
  private int frequency;

  @Persistent(mappedBy = "userMap")
  @Element(dependent = "true")
  public List<Feature> features = new ArrayList<Feature>();

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

  public int getFrequency() {
	return frequency;
  }

  public void setFrequency(int frequency) {
	this.frequency = frequency;
  }

  public List<Feature> getFeatures() {
	return features;
  }

  public void setFeatures(List<Feature> features) {
	this.features = features;
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
	return "UserMap [id=" + id + ", tqqUserId=" + tqqUserId + ", sinaUserId=" + sinaUserId + ", frequency=" + frequency + ", features=" + features + ", createTime=" + createTime + ", creator="
	    + creator + ", updateTime=" + updateTime + ", updator=" + updator + ", disable=" + disable + "]";
  }
  
  //SINAからTQQへメッセージの同期化制御値
  public boolean isSinaToTqq() {
	return Boolean.valueOf(getFeatureValue(Feature.Name.SINA_TO_TQQ));
  }

  //SINAからTQQへメッセージの同期化制御値
  public boolean isTqqToSina() {
	return Boolean.valueOf(getFeatureValue(Feature.Name.TQQ_TO_SINA));
  }
  
  //自動リトライするか？
  public boolean isAutoRetry() {
	return Boolean.valueOf(getFeatureValue(Feature.Name.AUTO_RETRY));
  }

  // メッセージ検証するか？
  public boolean isNeededMessageVirify() {
	return Boolean.valueOf(getFeatureValue(Feature.Name.MESSAGE_VIRIFY));
  }
  
  //SINAからTQQへコメントの同期化制御値
  public boolean isSinaToTqqComment() {
	return Boolean.valueOf(getFeatureValue(Feature.Name.SINA_TO_TQQ_COMMENT));
  }

  public String getFeatureValue(Feature.Name featureName) {
	if (this.features != null) {
	  for (Feature feature : this.features) {
		if (feature.getName().equals(Feature.Name.MESSAGE_VIRIFY)) {
		  return feature.getValue();
		}
	  }
	}
	return null;
  }
}
