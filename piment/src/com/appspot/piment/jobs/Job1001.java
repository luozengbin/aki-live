package com.appspot.piment.jobs;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.JobDao;
import com.appspot.piment.dao.PMF;
import com.appspot.piment.model.Job;
import com.appspot.piment.model.JobStatus;
import com.appspot.piment.util.DateUtils;

public class Job1001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1001.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("-- job1001 start --");
	long startTime = System.currentTimeMillis();
	Job job = null;
	try {

	  job = (new JobDao()).getJob(this.getClass().getName());

	  job.setStatus(JobStatus.RUNNING);
	  PMF.saveEntity(job);
	  log.info("job's status:" + job);

	  JobService jobService = new JobService();
	  jobService.run(job);

	  // ジョブ状態変更
	  job.setStatus(JobStatus.SUCCESSED);

	} catch (Exception e) {
	  // ジョブ状態変更
	  if (job != null) {
		job.setStatus(JobStatus.FAILED);
	  }
	  throw new RuntimeException(e);
	} finally {

	  long costTime = System.currentTimeMillis() - startTime;
	  if (job != null) {
		job.setLastExecuteTime(DateUtils.getSysDate());
		job.setCostTime(costTime);
		PMF.saveEntity(job);
		log.info("job's status:" + job);
	  }
	  log.info("-- job1001 end [cost " + costTime + " TimeMillis]--");
	}
  }

}
