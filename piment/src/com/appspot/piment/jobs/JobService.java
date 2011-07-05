package com.appspot.piment.jobs;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.Job;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboSource;

public class JobService {

  private static final Logger log = Logger.getLogger(Constants.FQCN + JobService.class.getName());

  private AuthTokenDao authTokenDao = null;
  private UserMapDao userMapDao = null;
  protected Map<String, String> configMap = null;
  private SinaMessageSync sinaMessageSync = null;

  public JobService() {
	super();

	this.authTokenDao = new AuthTokenDao();

	this.userMapDao = new UserMapDao();

	ConfigItemDao configItemDao = new ConfigItemDao();
	this.configMap = configItemDao.getValues();

	// tqqロボットユーザIDを元にAccessTokenを取り出す
	AuthToken tqqRobotAuthToken = authTokenDao.getByUserId(this.configMap.get("qq.piment.robot.id"), WeiboSource.Tqq);

	this.sinaMessageSync = new SinaMessageSync(this.configMap);
	this.sinaMessageSync.setTqqRobotToken(tqqRobotAuthToken);
  }

  public void run(Job job) {

	// 処理対象ユーザ一覧をデータストアから取得する
	List<UserMap> uerMaps = userMapDao.getUserMaps(job.getFrequency());

	// ユーザ毎に同期化処理を行う
	for (UserMap user : uerMaps) {

	  try {
		// sinaのユーザIDを元にAccessTokenを取り出す
		AuthToken sinaAuthToken = authTokenDao.getByUserId(user.getSinaUserId(), WeiboSource.Sina);
		// tqqのユーザIDを元にAccessTokenを取り出す
		AuthToken tqqAuthToken = authTokenDao.getByUserId(user.getTqqUserId(), WeiboSource.Tqq);

		// トークン情報渡す
		this.sinaMessageSync.setSinaToken(sinaAuthToken);
		this.sinaMessageSync.setTqqToken(tqqAuthToken);

		// [ST001 START] - SINAからTQQへ同期化判定 -
		if (user.sinaToTqq()) {
		  log.info("sina[" + user.getSinaUserId() + "] から tqq[" + user.getTqqUserId() + "]へ同期化開始");

		  // ユーザの設定よりリトライ処理の判定
		  if (user.isAutoRetry()) {
			this.sinaMessageSync.retrySyncUserMessage(user);
		  }
		  this.sinaMessageSync.syncUserMessage(user);

		} // -- [ST001 END]
	  } catch (Exception e) {
		String msg001 = "sina[" + user.getSinaUserId() + "] から tqq[" + user.getTqqUserId() + "]へ同期化開始中不具合が起きました";
		log.severe(msg001);
		log.severe(JSON.encode(e, true));

		e.printStackTrace();
		// 例外が起きても次ぎのメッセージの同期化を行う
	  }
	}

  }

}
