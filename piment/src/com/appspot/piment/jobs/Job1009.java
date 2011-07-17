package com.appspot.piment.jobs;

/**
 * データ掃除ジョブ
 *
 */
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.CommentMapDao;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.dao.JobDao;
import com.appspot.piment.dao.PMF;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.Job;
import com.appspot.piment.model.JobStatus;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.util.DateUtils;

public class Job1009 extends HttpServlet {

  private static final long serialVersionUID = -92121442307525087L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1009.class.getName());

  Map<String, String> configMap = null;

  public Job1009() {
	super();
	ConfigItemDao configItemDao = new ConfigItemDao();
	this.configMap = configItemDao.getValues();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {
	log.info("-- Job1009 start --");

	long startTime = System.currentTimeMillis();
	Job job = null;
	try {

	  job = (new JobDao()).getJob(this.getClass().getName());

	  job.setStatus(JobStatus.RUNNING);
	  PMF.saveEntity(job);
	  log.info("job's status:" + job);

	  // トークン情報の削除
	  AuthTokenDao authTokenDao = new AuthTokenDao();
	  authTokenDao.clearTempToken(Integer.valueOf(this.configMap.get("app.piment.temptoken.lifetime")));

	  UserMapDao userMapDao = new UserMapDao();
	  List<UserMap> allUserMap = userMapDao.getAllUserMaps();

	  // メッセージ履歴の掃除
	  WeiboMapDao weiboMapDao = new WeiboMapDao();
	  weiboMapDao.removeOlder(allUserMap, Integer.valueOf(this.configMap.get("app.piment.weibomap.lifetime")));

	  // コメント履歴の掃除
	  CommentMapDao commentMapDao = new CommentMapDao();
	  commentMapDao.removeOlder(allUserMap, Integer.valueOf(this.configMap.get("app.piment.weibomap.lifetime")));

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
	  log.info("-- Job1009 end [cost " + costTime + " TimeMillis]--");
	}

  }
}
