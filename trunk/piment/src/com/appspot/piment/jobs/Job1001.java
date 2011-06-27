package com.appspot.piment.jobs;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.InitDataDao;
import com.appspot.piment.dao.JobStatusDao;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.JobStatus;
import com.appspot.piment.model.UserMap;

public class Job1001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1001.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("-- job1001 start --");

	(new InitDataDao()).initUserMap();

	JobStatusDao jobStatusDao = new JobStatusDao();

	JobStatus jobStatus = jobStatusDao.getJobStatus(Job1001.class.getName());

	log.info(jobStatus.toString());

	com.appspot.piment.api.sina.WeiboApi sinaWeiboApi = null;
	AuthTokenDao authTokenDao = new AuthTokenDao();

	// ユーザ毎に同期化処理を行う
	for (UserMap userMap : (new UserMapDao()).getAllUserMaps()) {
	  // sinaのユーザIDを元にAccessTokenを取り出す
	  AuthToken sinaAuthToken = authTokenDao.getByUserId(userMap.getSinaUserId());

	  // AccessTokenでAPIオブジェクトを初期化する
	  sinaWeiboApi = new com.appspot.piment.api.sina.WeiboApi(sinaAuthToken);

	  List<Status> userMessages = sinaWeiboApi.getUserTimeline(userMap);
	  for (Status status : userMessages) {
		System.out.println(status.getText());
	  }
	}

	log.info("-- job1001 end --");

  }
}
