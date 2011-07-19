package com.appspot.piment.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;
import weibo4j.Comment;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.api.sina.SinaWeiboApi;
import com.appspot.piment.api.tqq.TqqWeiboApi;
import com.appspot.piment.api.tqq.model.MessageData;
import com.appspot.piment.api.tqq.model.MessageResponse;
import com.appspot.piment.api.tqq.model.Response;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.CommentMapDao;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.CommentMap;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.MailUtils;

public class TqqMessageSync {

  private static final Logger log = Logger.getLogger(Constants.FQCN + TqqMessageSync.class.getName());

  private Map<String, String> configMap = null;
  private WeiboMapDao weiboMapDao = null;
  private CommentMapDao commentMapDao = null;
  private UserMapDao userMapDao = null;
  private AuthTokenDao authTokenDao = null;

  private SinaWeiboApi sinaRobotWeiboApi = null;
  private SinaWeiboApi sinaTempWeiboApi = null;
  private SinaWeiboApi sinaWeiboApi = null;

  private TqqWeiboApi tqqWeiboApi = null;

  public TqqMessageSync(Map<String, String> configMap) {
	this.configMap = configMap;
	this.sinaRobotWeiboApi = new SinaWeiboApi(this.configMap);
	this.sinaTempWeiboApi = new SinaWeiboApi(this.configMap);
	this.sinaWeiboApi = new SinaWeiboApi(this.configMap);
	this.tqqWeiboApi = new TqqWeiboApi(this.configMap);
	this.weiboMapDao = new WeiboMapDao();
	this.commentMapDao = new CommentMapDao();
	this.userMapDao = new UserMapDao();
	this.authTokenDao = new AuthTokenDao();
  }

  public void setSinaRobotToken(AuthToken authToken) {
	this.sinaRobotWeiboApi.setAuthToken(authToken);
  }

  public void setTqqToken(AuthToken authToken) {
	this.tqqWeiboApi.setAuthToken(authToken);
  }

  public void setSinaToken(AuthToken authToken) {
	this.sinaWeiboApi.setAuthToken(authToken);
  }

  public List<WeiboMap> syncUserMessage(UserMap user) {

	try {

	  // 前回同期化された最後の履歴レコードを取り出す
	  WeiboMap lastestCreateWeiboMap = weiboMapDao.getNewestItem(user.getId(), WeiboSource.Tqq);

	  // sinaから前回の同期化以降対象ユーザが発表した新メッセージを取得する
	  List<MessageResponse> newUserMessages = tqqWeiboApi.getUserTimeline(lastestCreateWeiboMap != null ? lastestCreateWeiboMap.getTqqWeiboId() : null);

	  log.info("Tqq message --> 同期化件数：" + newUserMessages.size());

	  // メッセージ単位で同期化処理を行う
	  MessageResponse msgResponse = null;
	  for (int i = newUserMessages.size() - 1; i >= 0; i--) {
		msgResponse = newUserMessages.get(i);

		if (this.weiboMapDao.getByTqqWeiboId(Long.valueOf(msgResponse.getData().getId())) == null) {
		  syncTqqUserMessage(user, msgResponse, new WeiboMap());
		} else {
		  log.info("Tqq message --> [" + msgResponse.getData().getId() + "] 同期化済みでスキップする。");
		}
	  }

	  return null;

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }

  private void syncTqqUserMessage(UserMap userMap, MessageResponse msgResponse, WeiboMap weiboMap) {
	MessageData msgData = msgResponse.getData();
	try {

	  log.info("Tqq Message --> [" + msgData.getId() + "]同期化中...");

	  // 同期化履歴レコードの初期化
	  weiboMap.setTqqWeiboId(Long.valueOf(msgData.getId()));
	  weiboMap.setSinaWeiboId(null);
	  weiboMap.setUserMapId(userMap.getId());
	  weiboMap.setSource(WeiboSource.Tqq);
	  weiboMap.setStatus(WeiboStatus.UNKNOW);

	  if (!msgResponse.isOK()) {
		weiboMap.setStatus(WeiboStatus.FAILED);
		log.severe("Tqq Message --> [" + msgData.getId() + "]同期化失敗しました");
		return;
	  }

	  // 同期化対象判定
	  if (userMap.isNeededMessageVirify()) {
		// 同期化不要のキーワードが合ったら処理をスキップする
		if (msgData.getOrigtext().contains(this.configMap.get("app.piment.unsync.keyword"))) {
		  weiboMap.setStatus(WeiboStatus.SKIPPED);
		  log.info("Tqq Message --> [" + msgData.getId() + "]同期化対象外とする");
		  return;
		}
	  }

	  // 同じメッセージをsinaへ発表する
	  Status status = null;
	  Throwable throwable = null;
	  String originalMsg = null;
	  try {
		// // 转发微博的处理
		// if (status.isRetweet()) {
		//
		// String retweetId = null;
		// Status retweetedStatus = status.getRetweeted_status();
		// log.info("Sina Message --> Retweet [" + retweetedStatus.getId() +
		// "]");
		//
		// WeiboMap processedWeibo =
		// this.weiboMapDao.getBySinaWeiboId(retweetedStatus.getId());
		// if (processedWeibo != null) {
		// retweetId = String.valueOf(processedWeibo.getTqqWeiboId());
		// } else {
		//
		// originalMsg =
		// sinaWeiboApi.getOriginalMsg(retweetedStatus.getText().trim());
		//
		// StringBuilder retweetMsg = new StringBuilder();
		// retweetMsg.append("Sina@").append(retweetedStatus.getUser().getName()).append("//");
		// retweetMsg.append(originalMsg);
		// // TODO 長さ判定
		// retweetMsg.append("//Sina源：").append(SinaWeiboApi.getStatusPageURL(retweetedStatus.getUser().getId(),
		// retweetedStatus.getId()));
		//
		// Response middleResponse = null;//
		// tqqRobotWeiboApi.sendMessage(retweetMsg.toString(),
		// // retweetedStatus.getOriginal_pic(),
		// // null);
		// if (middleResponse != null && middleResponse.isOK()) {
		// log.info("Sina Message --> Retweet Successed!!!");
		// // データストアへ保存する
		//
		// // 同期化履歴レコードの初期化
		// WeiboMap retweetWeiboMap = new WeiboMap();
		// retweetWeiboMap.setSinaWeiboId(retweetedStatus.getId());
		// retweetWeiboMap.setTqqWeiboId(Long.valueOf(middleResponse.getData().getId()));
		// retweetWeiboMap.setUserMapId(null);
		// retweetWeiboMap.setSource(WeiboSource.Sina);
		// retweetWeiboMap.setStatus(WeiboStatus.SUCCESSED);
		// weiboMapDao.save(retweetWeiboMap);
		// retweetId = middleResponse.getData().getId();
		// }
		// }
		// if (retweetId != null) {
		// originalMsg = sinaWeiboApi.getOriginalMsg(status.getText().trim());
		// response = tqqWeiboApi.retweetMessage(retweetId, originalMsg,
		// status.getOriginal_pic(), null);
		// }
		//
		// } else { // 转发微博的处理　-　END
		//

		// 普通微博的处理

		// originalMsg = sinaWeiboApi.getOriginalMsg(status.getText().trim());
		// response = tqqWeiboApi.sendMessage(status.getText().trim(),
		// status.getOriginal_pic(), null);
		// }

		status = sinaWeiboApi.updateStatus(msgData.getOrigtext(), null);

	  } catch (Exception e) {
		throwable = e;
	  }

	  // 処理成功ならば、同期化レコードをデータストアへ保存する
	  if (status != null) {

		// 同期成功情報を履歴レコードに反映
		weiboMap.setStatus(WeiboStatus.SUCCESSED);
		weiboMap.setSinaWeiboId(Long.valueOf(status.getId()));

		log.info("Tqq Message --> 同期化成功！！！");
		log.info("Tqq Message --> メッセージID：" + msgData.getId());

	  } else {

		log.warning("Tqq Message --> 同期化失敗！！！");
		log.warning("Tqq Message --> メッセージID：" + msgData.getId());

		if (weiboMap.getId() != null) {
		  weiboMap.setRetryCount(weiboMap.getRetryCount() + 1);
		  // 失敗フラグを設定
		  if (weiboMap.getRetryCount() >= Integer.valueOf(this.configMap.get("app.sync.message.max.retry"))) {
			weiboMap.setStatus(WeiboStatus.ABORT);
		  } else {
			weiboMap.setStatus(WeiboStatus.FAILED);
		  }
		} else {
		  weiboMap.setStatus(WeiboStatus.FAILED);
		}

		String msg001 = "Tqq Message --> [" + msgData.getId() + "]メッセージをSinaへの送信が失敗しました。";
		log.severe(msg001);

		String errorDetail = throwable != null ? JSON.encode(throwable, true) : msgData.toString();
		log.severe(errorDetail);

		MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + msgData.toString() + "\n\nSinaからのレスポンス：\n" + errorDetail + "\n\n");
	  }

	} catch (Exception e) {

	  String msg001 = "Tqq Message --> 同期化失敗しました、メッセージID：" + msgData.getId();
	  log.severe(msg001);
	  log.severe(JSON.encode(e, true));
	  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + msgData.toString() + "\n\n例外：\n" + JSON.encode(e, true));
	  // 例外が起きても次ぎのメッセージの同期化を行う
	} finally {
	  // 同期化履歴レコードを保存する
	  weiboMap = weiboMapDao.save(weiboMap);
	  log.info("Tqq Message --> 同期化履歴レコードID：" + weiboMap.getId());
	}
  }
}
