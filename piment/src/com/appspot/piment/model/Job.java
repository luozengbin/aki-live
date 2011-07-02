package com.appspot.piment.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Job {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String jobId;

  @Persistent
  private String jobName;

  @Persistent
  private JobStatus status;

  @Persistent
  private String description;

  /** ジョブ動作の頻度 */
  @Persistent
  private int frequency;

  @Persistent
  private Date lastExecuteTime;

  @Persistent
  private long costTime;

  public Job() {
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

  public JobStatus getStatus() {
	return status;
  }

  public void setStatus(JobStatus status) {
	this.status = status;
  }

  public String getDescription() {
	return description;
  }

  public void setDescription(String description) {
	this.description = description;
  }

  public int getFrequency() {
	return frequency;
  }

  public void setFrequency(int frequency) {
	this.frequency = frequency;
  }

  public Date getLastExecuteTime() {
	return lastExecuteTime;
  }

  public void setLastExecuteTime(Date lastExecuteTime) {
	this.lastExecuteTime = lastExecuteTime;
  }

  public long getCostTime() {
	return costTime;
  }

  public void setCostTime(long costTime) {
	this.costTime = costTime;
  }

  @Override
  public String toString() {
	return "Job [id=" + id + ", jobId=" + jobId + ", jobName=" + jobName + ", status=" + status + ", description=" + description + ", frequency=" + frequency + ", lastExecuteTime=" + lastExecuteTime
	    + ", costTime=" + costTime + "]";
  }

}
