package com.appspot.piment.dao;

import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.Job;
import com.appspot.piment.model.JobStatus;
import com.appspot.piment.util.DateUtils;

public class JobDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + Job.class.getName() + " where jobId == :jobId";

  private PersistenceManager pm = null;

  public JobDao() {
	super();
  }

  public Job getJob(String jobId) {

	Job result = null;

	try {
	  pm = PMF.get().getPersistenceManager();

	  @SuppressWarnings("unchecked")
	  List<Job> jobStatusList = (List<Job>) pm.newQuery(QL_001).execute(jobId);

	  if (jobStatusList == null || jobStatusList.size() == 0) {

		result = new Job();
		result.setJobId(jobId);
		result.setJobName(jobId);
		result.setStatus(JobStatus.UNKNOW);
		result.setDescription(jobId + "'s description");
		result.setLastExecuteTime(DateUtils.getSysDate(Calendar.MINUTE, -30));
		result.setCostTime(0);
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
