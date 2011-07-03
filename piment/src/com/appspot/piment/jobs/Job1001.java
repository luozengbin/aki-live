package com.appspot.piment.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.api.tqq.Response;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.dao.JobDao;
import com.appspot.piment.dao.PMF;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.Job;
import com.appspot.piment.model.JobStatus;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;
import com.appspot.piment.util.MailUtils;

public class Job1001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1001.class.getName());

  private AuthTokenDao authTokenDao = null;
  private WeiboMapDao weiboMapDao = null;
  private UserMapDao userMapDao = null;
  private ConfigItemDao configItemDao = null;
  private JobDao jobDao = null;
  private com.appspot.piment.api.tqq.WeiboApi tqqRobotWeiboApi = null;

  public Job1001() {
	super();
	this.authTokenDao = new AuthTokenDao();
	this.weiboMapDao = new WeiboMapDao();
	this.userMapDao = new UserMapDao();
	this.configItemDao = new ConfigItemDao();
	this.jobDao = new JobDao();

	// tqqロボットユーザIDを元にAccessTokenを取り出す
	AuthToken tqqRobotAuthToken = authTokenDao.getByUserId(this.configItemDao.getValue("qq.piment.robot.id"));
	// tqqロボットのAccessTokenでAPIオブジェクトを初期化する
	this.tqqRobotWeiboApi = new com.appspot.piment.api.tqq.WeiboApi(tqqRobotAuthToken);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("-- job1001 start --");
	long startTime = System.currentTimeMillis();
	Job job = null;
	try {

	  com.appspot.piment.api.sina.WeiboApi sinaWeiboApi = null;
	  com.appspot.piment.api.tqq.WeiboApi tqqWeiboApi = null;

	  job = this.jobDao.getJob(this.getClass().getName());

	  job.setStatus(JobStatus.RUNNING);
	  PMF.saveEntity(job);
	  log.info("job's status:" + job);

	  // 処理対象ユーザ一覧をデータストアから取得する
	  List<UserMap> uerMaps = userMapDao.getUserMaps(job.getFrequency());

	  // ユーザ毎に同期化処理を行う
	  for (UserMap userMap : uerMaps) {
		try {
		  log.info("sina[" + userMap.getSinaUserId() + "] から tqq[" + userMap.getTqqUserId() + "]へ同期化開始");

		  // sinaのユーザIDを元にAccessTokenを取り出す
		  AuthToken sinaAuthToken = authTokenDao.getByUserId(userMap.getSinaUserId());

		  // AccessTokenでAPIオブジェクトを初期化する
		  sinaWeiboApi = new com.appspot.piment.api.sina.WeiboApi(sinaAuthToken);

		  // tqqのユーザIDを元にAccessTokenを取り出す
		  AuthToken tqqAuthToken = authTokenDao.getByUserId(userMap.getTqqUserId());

		  // AccessTokenでAPIオブジェクトを初期化する
		  tqqWeiboApi = new com.appspot.piment.api.tqq.WeiboApi(tqqAuthToken);

		  // ユーザの設定よりリトライ処理の判定
		  if (userMap.isRetryAction()) {

			// リトライ処理を行う
			List<WeiboMap> retryWeiboMaps = this.weiboMapDao.getFieldItem(userMap.getId());

			if (retryWeiboMaps.size() > 0) {

			  Long startId = retryWeiboMaps.get(0).getSinaWeiboId() - 1;
			  Long endId = retryWeiboMaps.get(retryWeiboMaps.size() - 1).getSinaWeiboId();

			  // リトライの対象メッセージを取得
			  List<Status> oldUserMessages = sinaWeiboApi.getUserTimeline(startId, endId);

			  Map<Long, Status> oldUserMessagesMap = new HashMap<Long, Status>();
			  for (Status oldUserMessage : oldUserMessages) {
				oldUserMessagesMap.put(oldUserMessage.getId(), oldUserMessage);
			  }

			  for (WeiboMap retryWeiboMap : retryWeiboMaps) {
				if (oldUserMessagesMap.containsKey(retryWeiboMap.getSinaWeiboId())) {
				  // RETRY
				  syncSinaUserMessage(tqqWeiboApi, userMap, oldUserMessagesMap.get(retryWeiboMap.getSinaWeiboId()), retryWeiboMap);

				} else {
				  // ABORT
				  retryWeiboMap.setStatus(WeiboStatus.ABORT);

				  // 同期化履歴レコードを保存する
				  retryWeiboMap = weiboMapDao.save(retryWeiboMap);
				}
			  }
			}
		  }

		  // 前回同期化された最後の履歴レコードを取り出す
		  WeiboMap lastestCreateWeiboMap = weiboMapDao.getNewestItem(userMap.getId());

		  // sinaから前回の同期化以降対象ユーザが発表した新メッセージを取得する
		  List<Status> newUserMessages = sinaWeiboApi.getUserTimeline(lastestCreateWeiboMap != null ? lastestCreateWeiboMap.getSinaWeiboId() : null, null);

		  log.info("同期化件数：" + newUserMessages.size());

		  // メッセージ単位で同期化処理を行う
		  Status status = null;
		  for (int i = newUserMessages.size() - 1; i >= 0; i--) {
			status = newUserMessages.get(i);
			syncSinaUserMessage(tqqWeiboApi, userMap, status, new WeiboMap());
		  }

		} catch (Exception e) {
		  String msg001 = "sina[" + userMap.getSinaUserId() + "] から tqq[" + userMap.getTqqUserId() + "]へ同期化開始中不具合が起きました";
		  log.severe(msg001);
		  log.severe(JSON.encode(e, true));

		  e.printStackTrace();
		  // 例外が起きても次ぎのメッセージの同期化を行う
		}
	  }

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

  private void syncSinaUserMessage(com.appspot.piment.api.tqq.WeiboApi tqqWeiboApi, UserMap userMap, Status status, WeiboMap weiboMap) {
	try {

	  log.info("sina[" + status.getId() + "]メッセージ同期化中...");

	  log.info(JSON.encode(status, true));

	  // テキストメッセージなら
	  if (StringUtils.isNotBlank(status.getText())) {

		// 同期化履歴レコードの初期化
		weiboMap.setSinaWeiboId(status.getId());
		weiboMap.setTqqWeiboId(null);
		weiboMap.setUserMapId(userMap.getId());
		weiboMap.setSource(WeiboSource.Sina);
		weiboMap.setStatus(WeiboStatus.UNKNOW);

		// 同じメッセージをtqqへ発表する
		Response response = null;
		Throwable throwable = null;
		try {
		  if (status.isRetweet()) {

			String retweetId = null;
			Status retweetedStatus = status.getRetweeted_status();
			log.info("Retweet sina[" + retweetedStatus.getId() + "]");

			WeiboMap processedWeibo = this.weiboMapDao.getBySinaWeiboId(retweetedStatus.getId());
			if (processedWeibo != null) {
			  retweetId = String.valueOf(processedWeibo.getTqqWeiboId());
			} else {
			  StringBuilder retweetMsg = new StringBuilder();
			  retweetMsg.append("转发自新浪微博用户 @").append(retweetedStatus.getUser().getName()).append(" \n");
			  retweetMsg.append(retweetedStatus.getText().trim()).append(" \n");
			  Response middleResponse = tqqRobotWeiboApi.sendMessage(retweetMsg.toString(), retweetedStatus.getOriginal_pic(), null);
			  if (middleResponse != null && middleResponse.isOK()) {
				log.info("Retweet Successed!!!");
				//データストアへ保存する
				
				// 同期化履歴レコードの初期化
				WeiboMap retweetWeiboMap = new WeiboMap();
				retweetWeiboMap.setSinaWeiboId(retweetedStatus.getId());
				retweetWeiboMap.setTqqWeiboId(Long.valueOf(middleResponse.getData().getId()));
				retweetWeiboMap.setUserMapId(null);
				retweetWeiboMap.setSource(WeiboSource.Sina);
				retweetWeiboMap.setStatus(WeiboStatus.SUCCESSED);
				weiboMapDao.save(retweetWeiboMap);
				retweetId = middleResponse.getData().getId();
			  }
			}
			if (retweetId != null) {
			  response = tqqWeiboApi.retweetMessage(retweetId, status.getText().trim(), status.getOriginal_pic(), null);
			}

		  } else {
			response = tqqWeiboApi.sendMessage(status.getText().trim(), status.getOriginal_pic(), null);
		  }
		} catch (Exception e) {
		  throwable = e;
		}

		// 処理成功ならば、同期化レコードをデータストアへ保存する
		if (response != null && response.isOK()) {

		  // 同期成功情報を履歴レコードに反映
		  weiboMap.setStatus(WeiboStatus.SUCCESSED);
		  weiboMap.setTqqWeiboId(Long.valueOf(response.getData().getId()));

		  log.info("同期化成功！！！");
		  log.info("メッセージID：" + status.getId());
		  log.info("同期化履歴レコードID：" + weiboMap.getId());
		} else {

		  if (weiboMap.getId() != null) {
			weiboMap.setRetryCount(weiboMap.getRetryCount() + 1);
			// 失敗フラグを設定
			if (weiboMap.getRetryCount() >= Integer.valueOf(this.configItemDao.getValue("app.sync.message.max.retry"))) {
			  weiboMap.setStatus(WeiboStatus.ABORT);
			} else {
			  weiboMap.setStatus(WeiboStatus.FAILED);
			}
		  } else {
			weiboMap.setStatus(WeiboStatus.FAILED);
		  }

		  String msg001 = "sina[" + status.getId() + "]メッセージをTQQへの送信が失敗しました。";
		  log.severe(msg001);

		  String errorDetail = throwable != null ? JSON.encode(throwable, true) : response.toString();
		  log.severe(errorDetail);

		  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\nTQQからのレスポンス：\n" + errorDetail + "\n\n");
		}

		// 同期化履歴レコードを保存する
		weiboMap = weiboMapDao.save(weiboMap);
	  }

	  // TODO 画像ファイル
	  // TODO ビデオ
	  // TODO 引用

	} catch (Exception e) {

	  String msg001 = "同期化失敗しました、メッセージID：" + status.getId();
	  log.severe(msg001);
	  log.severe(JSON.encode(e, true));
	  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\n例外：\n" + JSON.encode(e, true));
	  // 例外が起きても次ぎのメッセージの同期化を行う
	}
  }
}
