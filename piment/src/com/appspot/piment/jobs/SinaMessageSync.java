package com.appspot.piment.jobs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.arnx.jsonic.JSON;
import weibo4j.Comment;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.api.tqq.Response;
import com.appspot.piment.dao.CommentMapDao;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.CommentMap;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.MailUtils;

public class SinaMessageSync {

  private static final Logger log = Logger.getLogger(Constants.FQCN + SinaMessageSync.class.getName());

  private Map<String, String> configMap = null;
  private WeiboMapDao weiboMapDao = null;
  private CommentMapDao commentMapDao = null;

  private com.appspot.piment.api.tqq.TqqWeiboApi tqqRobotWeiboApi = null;
  private com.appspot.piment.api.tqq.TqqWeiboApi tqqWeiboApi = null;
  private com.appspot.piment.api.sina.SinaWeiboApi sinaWeiboApi = null;

  public SinaMessageSync(Map<String, String> configMap) {
	this.configMap = configMap;

	this.tqqRobotWeiboApi = new com.appspot.piment.api.tqq.TqqWeiboApi(this.configMap);

	this.tqqWeiboApi = new com.appspot.piment.api.tqq.TqqWeiboApi(this.configMap);
	this.sinaWeiboApi = new com.appspot.piment.api.sina.SinaWeiboApi(this.configMap);

	this.weiboMapDao = new WeiboMapDao();
	this.commentMapDao = new CommentMapDao();
  }

  public void setTqqRobotToken(AuthToken authToken) {
	this.tqqRobotWeiboApi.setAuthToken(authToken);
  }

  public void setTqqToken(AuthToken authToken) {
	this.tqqWeiboApi.setAuthToken(authToken);
  }

  public void setSinaToken(AuthToken authToken) {
	this.sinaWeiboApi.setAuthToken(authToken);
  }

  public List<WeiboMap> retrySyncUserMessage(UserMap user) {

	// リトライ処理を行う
	List<WeiboMap> retryWeiboMaps = this.weiboMapDao.getFieldItem(user.getId());

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
		  syncSinaUserMessage(user, oldUserMessagesMap.get(retryWeiboMap.getSinaWeiboId()), retryWeiboMap);

		} else {
		  // ABORT
		  retryWeiboMap.setStatus(WeiboStatus.ABORT);

		  // 同期化履歴レコードを保存する
		  retryWeiboMap = weiboMapDao.save(retryWeiboMap);
		}
	  }
	}
	return null;
  }

  public List<WeiboMap> syncUserMessage(UserMap user) {

	// 前回同期化された最後の履歴レコードを取り出す
	WeiboMap lastestCreateWeiboMap = weiboMapDao.getNewestItem(user.getId());

	// sinaから前回の同期化以降対象ユーザが発表した新メッセージを取得する
	List<Status> newUserMessages = sinaWeiboApi.getUserTimeline(lastestCreateWeiboMap != null ? lastestCreateWeiboMap.getSinaWeiboId() : null, null);

	log.info("同期化件数：" + newUserMessages.size());

	// メッセージ単位で同期化処理を行う
	Status status = null;
	for (int i = newUserMessages.size() - 1; i >= 0; i--) {
	  status = newUserMessages.get(i);
	  syncSinaUserMessage(user, status, new WeiboMap());
	}
	return null;
  }

  public List<CommentMap> syncUserComment(UserMap user) {

	// 前回同期化された最後のコメント履歴レコードを取り出す
	CommentMap lastestCreateCommentMap = commentMapDao.getNewestItem(user.getId());

	// sinaから前回の同期化以降の新コメントを取得する
	List<Comment> newComments = sinaWeiboApi.getCommentTimeline(lastestCreateCommentMap != null ? lastestCreateCommentMap.getSinaCommentId() : null, null);

	// メッセージ単位で同期化処理を行う
	Comment comment = null;
	for (int i = newComments.size() - 1; i >= 0; i--) {
	  comment = newComments.get(i);
	  log.info("new comment -->" + JSON.encode(comment, true));
	}

	return null;
  }

  private void syncSinaUserMessage(UserMap userMap, Status status, WeiboMap weiboMap) {
	try {

	  log.info("sina[" + status.getId() + "]メッセージ同期化中...");

	  log.info(JSON.encode(status, true));

	  // テキストメッセージなら
	  if (StringUtils.isNotBlank(status.getText())) {

		// 同期化対象判定
		if (userMap.isNeededMessageVirify()) {
		  // 同期化不要のキーワードが合ったら処理をスキップする
		  if (status.getText().contains(this.configMap.get("app.piment.unsync.keyword"))) {
			weiboMap.setSinaWeiboId(status.getId());
			weiboMap.setTqqWeiboId(null);
			weiboMap.setUserMapId(userMap.getId());
			weiboMap.setSource(WeiboSource.Sina);
			weiboMap.setStatus(WeiboStatus.SKIPPED);
			log.info("sina[" + status.getId() + "]メッセージ同期化対象外とする");
			return;
		  }
		}

		// 同期化履歴レコードの初期化
		weiboMap.setSinaWeiboId(status.getId());
		weiboMap.setTqqWeiboId(null);
		weiboMap.setUserMapId(userMap.getId());
		weiboMap.setSource(WeiboSource.Sina);
		weiboMap.setStatus(WeiboStatus.UNKNOW);

		// 同じメッセージをtqqへ発表する
		Response response = null;
		Throwable throwable = null;
		String originalMsg = null;
		try {
		  // 转发微博的处理
		  if (status.isRetweet()) {

			String retweetId = null;
			Status retweetedStatus = status.getRetweeted_status();
			log.info("Retweet sina[" + retweetedStatus.getId() + "]");

			WeiboMap processedWeibo = this.weiboMapDao.getBySinaWeiboId(retweetedStatus.getId());
			if (processedWeibo != null) {
			  retweetId = String.valueOf(processedWeibo.getTqqWeiboId());
			} else {

			  originalMsg = sinaWeiboApi.getOriginalMsg(retweetedStatus.getText().trim());

			  StringBuilder retweetMsg = new StringBuilder();
			  retweetMsg.append("转自Sina//@").append(retweetedStatus.getUser().getName()).append("//");
			  retweetMsg.append(originalMsg);
			  // TODO 長さ判定
			  retweetMsg.append("//源链接：").append(com.appspot.piment.api.sina.SinaWeiboApi.getStatusPageURL(retweetedStatus.getUser().getId(), retweetedStatus.getId()));

			  Response middleResponse = tqqRobotWeiboApi.sendMessage(retweetMsg.toString(), retweetedStatus.getOriginal_pic(), null);
			  if (middleResponse != null && middleResponse.isOK()) {
				log.info("Retweet Successed!!!");
				// データストアへ保存する

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
			  originalMsg = sinaWeiboApi.getOriginalMsg(status.getText().trim());
			  response = tqqWeiboApi.retweetMessage(retweetId, originalMsg, status.getOriginal_pic(), null);
			}

		  } else { // 转发微博的处理　-　END

			// 普通微博的处理
			originalMsg = sinaWeiboApi.getOriginalMsg(status.getText().trim());
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

		} else {

		  log.warning("同期化失敗！！！");
		  log.warning("メッセージID：" + status.getId());

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

		  String msg001 = "sina[" + status.getId() + "]メッセージをTQQへの送信が失敗しました。";
		  log.severe(msg001);

		  String errorDetail = throwable != null ? JSON.encode(throwable, true) : response.toString();
		  log.severe(errorDetail);

		  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\nTQQからのレスポンス：\n" + errorDetail + "\n\n");
		}
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
	} finally {
	  // 同期化履歴レコードを保存する
	  weiboMap = weiboMapDao.save(weiboMap);
	  log.info("同期化履歴レコードID：" + weiboMap.getId());
	}
  }
}
