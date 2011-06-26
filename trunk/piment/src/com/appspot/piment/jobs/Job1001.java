package com.appspot.piment.jobs;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.JobStatusDao;
import com.appspot.piment.model.JobStatus;

public class Job1001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1001.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("-- job1001 start --");

	JobStatusDao jobStatusDao = new JobStatusDao();

	JobStatus jobStatus = jobStatusDao.getJobStatus(Job1001.class.getName());
	
	log.info(jobStatus.toString());

	log.info("-- job1001 end --");

  }
}
