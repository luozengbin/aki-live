package com.appspot.piment.dao;

import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.JobStatus;
import com.appspot.piment.util.DateUtils;

public class JobStatusDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + JobStatus.class.getName() + " where jobId == :jobId";

  private PersistenceManager pm = null;

  public JobStatusDao() {
	super();
  }

  public JobStatus getJobStatus(String jobId) {

	JobStatus result = null;

	try {
	  pm = PMF.get().getPersistenceManager();

	  @SuppressWarnings("unchecked")
	  List<JobStatus> jobStatusList = (List<JobStatus>) pm.newQuery(QL_001).execute(jobId);

	  if (jobStatusList == null || jobStatusList.size() == 0) {

		result = new JobStatus();
		result.setJobId(jobId);
		result.setJobName(jobId);
		result.setDescription(jobId + "'s description");
		result.setLastExecuteTime(DateUtils.getSysDate(Calendar.MINUTE, -30));
		result.setLastExecuteResult("UNKNOW");
		result = pm.makePersistent(result);

	  } else {
		result = jobStatusList.get(0);
	  }

	  return result;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

}
