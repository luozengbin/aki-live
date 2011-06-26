package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JobStatus {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String jobId;

  @Persistent
  private String jobName;

  @Persistent
  private String description;

  @Persistent
  private Date lastExecuteTime;

  @Persistent
  private String lastExecuteResult;

  public JobStatus() {
	super();
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }

  public String getJobId() {
	return jobId;
  }

  public void setJobId(String jobId) {
	this.jobId = jobId;
  }

  public String getJobName() {
	return jobName;
  }

  public void setJobName(String jobName) {
	this.jobName = jobName;
  }

  public String getDescription() {
	return description;
  }

  public void setDescription(String description) {
	this.description = description;
  }

  public Date getLastExecuteTime() {
	return lastExecuteTime;
  }

  public void setLastExecuteTime(Date lastExecuteTime) {
	this.lastExecuteTime = lastExecuteTime;
  }

  public String getLastExecuteResult() {
	return lastExecuteResult;
  }

  public void setLastExecuteResult(String lastExecuteResult) {
	this.lastExecuteResult = lastExecuteResult;
  }

  @Override
  public String toString() {
	return "JobStatus [id=" + id + ", jobId=" + jobId + ", jobName=" + jobName + ", description=" + description + ", lastExecuteTime=" + lastExecuteTime + ", lastExecuteResult=" + lastExecuteResult
	    + "]";
  }

}
