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
import com.appspot.piment.api.tqq.Response;
import com.appspot.piment.api.tqq.TqqWeiboApi;
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

public class SinaMessageSync {

  private static final Logger log = Logger.getLogger(Constants.FQCN + SinaMessageSync.class.getName());

  private Map<String, String> configMap = null;
  private WeiboMapDao weiboMapDao = null;
  private CommentMapDao commentMapDao = null;
  private UserMapDao userMapDao = null;
  private AuthTokenDao authTokenDao = null;

  private TqqWeiboApi tqqRobotWeiboApi = null;
  private TqqWeiboApi tqqTempWeiboApi = null;
  private TqqWeiboApi tqqWeiboApi = null;
  private SinaWeiboApi sinaWeiboApi = null;

  public SinaMessageSync(Map<String, String> configMap) {
	this.configMap = configMap;
	this.tqqRobotWeiboApi = new TqqWeiboApi(this.configMap);
	this.tqqTempWeiboApi = new TqqWeiboApi(this.configMap);
	this.tqqWeiboApi = new TqqWeiboApi(this.configMap);
	this.sinaWeiboApi = new SinaWeiboApi(this.configMap);
	this.weiboMapDao = new WeiboMapDao();
	this.commentMapDao = new CommentMapDao();
	this.userMapDao = new UserMapDao();
	this.authTokenDao = new AuthTokenDao();
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

	log.info("Sina message --> 同期化件数：" + newUserMessages.size());

	// メッセージ単位で同期化処理を行う
	Status status = null;
	for (int i = newUserMessages.size() - 1; i >= 0; i--) {
	  status = newUserMessages.get(i);
	  
	  if(this.weiboMapDao.getBySinaWeiboId(status.getId()) == null){
		syncSinaUserMessage(user, status, new WeiboMap());
	  }else{
		log.info("Sina message --> [" + status.getId() + "] 同期化済みでスキップする。");
	  }
	}
	return null;
  }

  public List<WeiboMap> retrySyncUserComment(UserMap user) {

	// リトライ処理を行う
	List<CommentMap> retryCommentMaps = this.commentMapDao.getFieldItem(user.getId());

	if (retryCommentMaps.size() > 0) {

	  Long startId = retryCommentMaps.get(0).getSinaCommentId() - 1;
	  Long endId = retryCommentMaps.get(retryCommentMaps.size() - 1).getSinaCommentId();

	  // リトライの対象コメントを取得
	  List<Comment> oldUserComments = sinaWeiboApi.getCommentTimeline(startId, endId);

	  Map<Long, Comment> oldUserCommentsMap = new HashMap<Long, Comment>();
	  for (Comment oldUserComment : oldUserComments) {
		oldUserCommentsMap.put(oldUserComment.getId(), oldUserComment);
	  }

	  for (CommentMap retryCommentMap : retryCommentMaps) {
		if (oldUserCommentsMap.containsKey(retryCommentMap.getSinaCommentId())) {
		  // RETRY
		  syncSinaUserComment(user, oldUserCommentsMap.get(retryCommentMap.getSinaCommentId()), retryCommentMap);

		} else {
		  // ABORT
		  retryCommentMap.setStatus(WeiboStatus.ABORT);

		  // 同期化履歴レコードを保存する
		  retryCommentMap = commentMapDao.save(retryCommentMap);
		}
	  }
	}
	return null;
  }

  public List<CommentMap> syncUserComment(UserMap user) {

	// 前回同期化された最後のコメント履歴レコードを取り出す
	CommentMap lastestCreateCommentMap = commentMapDao.getNewestItem(user.getId());

	// sinaから前回の同期化以降の新コメントを取得する
	List<Comment> newComments = sinaWeiboApi.getCommentTimeline(lastestCreateCommentMap != null ? lastestCreateCommentMap.getSinaCommentId() : null, null);

	log.info("Sina comment --> 同期化件数：" + newComments.size());

	// メッセージ単位で同期化処理を行う
	Comment comment = null;

	for (int i = newComments.size() - 1; i >= 0; i--) { // FOR-101
	  comment = newComments.get(i);
	  if(this.commentMapDao.getBySinaCommentId(comment.getId()) == null){
		syncSinaUserComment(user, comment, new CommentMap());
	  }else{
		log.info("Sina comment --> [" + comment.getId() + "] 同期化済みでスキップする。");
	  }
	}
	return null;
  }

  private void syncSinaUserComment(UserMap user, Comment comment, CommentMap commentMap) {

	log.info("Sina comment --> [" + comment.getId() + "]同期化中...");

	commentMap.setSinaCommentId(comment.getId());
	commentMap.setTqqCommentId(null);
	commentMap.setUserMapId(user.getId());
	commentMap.setSource(WeiboSource.Sina);
	commentMap.setStatus(WeiboStatus.UNKNOW);
	commentMap.setWeiboId(comment.getStatus().getId());

	try {

	  // 同期化対象判定
	  if (user.isNeededMessageVirify()) {
		// 同期化不要のキーワードが合ったら処理をスキップする
		if (comment.getText().contains(this.configMap.get("app.piment.unsync.keyword"))) {
		  commentMap.setStatus(WeiboStatus.SKIPPED);
		  log.info("Sina comment --> [" + comment.getId() + "]同期化対象外とする");
		  return;
		}
	  }

	  Long sinaWeiboId = comment.getStatus().getId();
	  WeiboMap weiboMap = this.weiboMapDao.getBySinaWeiboId(sinaWeiboId);

	  if (weiboMap != null && StringUtils.isNotBlank(String.valueOf(weiboMap.getTqqWeiboId()))) {

		Response response = null;
		Throwable throwable = null;

		TqqWeiboApi tqqApi = null;

		String commentUserId = String.valueOf(comment.getUser().getId());
		String tqqWeiboId = String.valueOf(weiboMap.getTqqWeiboId());
		String commentMsg = comment.getText();

		if (commentUserId.equals(this.tqqWeiboApi.getUsetId())) {
		  tqqApi = this.tqqWeiboApi;
		} else {
		  UserMap userMap = this.userMapDao.getUserMap(commentUserId);
		  if (userMap != null && StringUtils.isNotBlank(userMap.getTqqUserId())) {
			AuthToken tempAuthToken = this.authTokenDao.getByUserId(userMap.getTqqUserId(), WeiboSource.Tqq);
			this.tqqTempWeiboApi.setAuthToken(tempAuthToken);
			tqqApi = this.tqqTempWeiboApi;
		  } else {
			tqqApi = this.tqqRobotWeiboApi;
			commentMsg = "Sina @" + comment.getUser().getScreenName() + "//" + commentMsg;
		  }
		}

		try {
		  response = tqqApi.sendComment(tqqWeiboId, commentMsg, null);
		} catch (Exception e) {
		  throwable = e;
		}

		// 処理成功ならば、同期化レコードをデータストアへ保存する
		if (response != null && response.isOK()) {
		  // 同期成功情報を履歴レコードに反映
		  commentMap.setStatus(WeiboStatus.SUCCESSED);
		  commentMap.setTqqCommentId(Long.valueOf(response.getData().getId()));
		  log.info("Sina comment --> コメント同期化成功！！！");
		  log.info("Sina comment --> コメントメッセージID：" + comment.getId());
		} else {

		  log.warning("Sina comment --> コメント同期化失敗！！！");
		  log.warning("Sina comment --> メッセージID：" + comment.getId());

		  if (commentMap.getId() != null) {
			commentMap.setRetryCount(commentMap.getRetryCount() + 1);
			// 失敗フラグを設定
			if (commentMap.getRetryCount() >= Integer.valueOf(this.configMap.get("app.sync.message.max.retry"))) {
			  commentMap.setStatus(WeiboStatus.ABORT);
			} else {
			  commentMap.setStatus(WeiboStatus.FAILED);
			}
		  } else {
			commentMap.setStatus(WeiboStatus.FAILED);
		  }

		  String msg001 = "Sina comment --> [" + commentMap.getId() + "]をTQQへの送信が失敗しました。";
		  log.severe(msg001);

		  String errorDetail = throwable != null ? JSON.encode(throwable, true) : response.toString();
		  log.severe(errorDetail);

		  MailUtils.sendErrorReport(msg001 + "\n\n処理コメント：" + comment.toString() + "\n\nTQQからのレスポンス：\n" + errorDetail + "\n\n");

		}
	  } else {
		commentMap.setStatus(WeiboStatus.SKIPPED);
		log.info("Sina comment --> [" + comment.getId() + "] 対応するメッセージ履歴がないため、同期化対象外とする");
	  }

	} catch (Exception e) {
	  String msg001 = "Sina comment --> 同期化失敗しました、コメントメッセージID：" + comment.getId();
	  log.severe(msg001);
	  log.severe(JSON.encode(e, true));
	  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + comment.toString() + "\n\n例外：\n" + JSON.encode(e, true));
	  // 例外が起きても次ぎのメッセージの同期化を行う
	} finally {
	  // 同期化履歴レコードを保存する
	  commentMap = commentMapDao.save(commentMap);
	  log.info("Sina comment --> 同期化履歴レコードID：" + commentMap.getId());
	}
  }

  private void syncSinaUserMessage(UserMap userMap, Status status, WeiboMap weiboMap) {
	try {

	  log.info("Sina Message --> [" + status.getId() + "]同期化中...");

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
			log.info("Sina Message --> [" + status.getId() + "]同期化対象外とする");
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
			log.info("Sina Message --> Retweet [" + retweetedStatus.getId() + "]");

			WeiboMap processedWeibo = this.weiboMapDao.getBySinaWeiboId(retweetedStatus.getId());
			if (processedWeibo != null) {
			  retweetId = String.valueOf(processedWeibo.getTqqWeiboId());
			} else {

			  originalMsg = sinaWeiboApi.getOriginalMsg(retweetedStatus.getText().trim());

			  StringBuilder retweetMsg = new StringBuilder();
			  retweetMsg.append("Sina@").append(retweetedStatus.getUser().getName()).append("//");
			  retweetMsg.append(originalMsg);
			  // TODO 長さ判定
			  retweetMsg.append("//Sina源：").append(SinaWeiboApi.getStatusPageURL(retweetedStatus.getUser().getId(), retweetedStatus.getId()));

			  Response middleResponse = tqqRobotWeiboApi.sendMessage(retweetMsg.toString(), retweetedStatus.getOriginal_pic(), null);
			  if (middleResponse != null && middleResponse.isOK()) {
				log.info("Sina Message --> Retweet Successed!!!");
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

		  log.info("Sina Message --> 同期化成功！！！");
		  log.info("Sina Message --> メッセージID：" + status.getId());

		} else {

		  log.warning("Sina Message --> 同期化失敗！！！");
		  log.warning("Sina Message --> メッセージID：" + status.getId());

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

		  String msg001 = "Sina Message --> [" + status.getId() + "]メッセージをTQQへの送信が失敗しました。";
		  log.severe(msg001);

		  String errorDetail = throwable != null ? JSON.encode(throwable, true) : response.toString();
		  log.severe(errorDetail);

		  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\nTQQからのレスポンス：\n" + errorDetail + "\n\n");
		}
	  }

	} catch (Exception e) {

	  String msg001 = "Sina Message --> 同期化失敗しました、メッセージID：" + status.getId();
	  log.severe(msg001);
	  log.severe(JSON.encode(e, true));
	  MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\n例外：\n" + JSON.encode(e, true));
	  // 例外が起きても次ぎのメッセージの同期化を行う
	} finally {
	  // 同期化履歴レコードを保存する
	  weiboMap = weiboMapDao.save(weiboMap);
	  log.info("Sina Message --> 同期化履歴レコードID：" + weiboMap.getId());
	}
  }
}
