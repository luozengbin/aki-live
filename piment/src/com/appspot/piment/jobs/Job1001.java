package com.appspot.piment.jobs;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.api.tqq.Response;
import com.appspot.piment.dao.AuthTokenDao;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;
import com.appspot.piment.util.MailUtils;

public class Job1001 extends HttpServlet {

  private static final long serialVersionUID = 1288120185298127312L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1001.class.getName());

  private AuthTokenDao authTokenDao = null;
  private WeiboMapDao weiboMapDao = null;
  private UserMapDao userMapDao = null;

  public Job1001() {
	super();
	authTokenDao = new AuthTokenDao();
	weiboMapDao = new WeiboMapDao();
	userMapDao = new UserMapDao();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("-- job1001 start --");

	try {

	  com.appspot.piment.api.sina.WeiboApi sinaWeiboApi = null;
	  com.appspot.piment.api.tqq.WeiboApi tqqWeiboApi = null;

	  // 処理対象ユーザ一覧をデータストアから取得する
	  List<UserMap> uerMaps = userMapDao.getAllUserMaps();

	  // ユーザ毎に同期化処理を行う
	  for (UserMap userMap : uerMaps) {

		log.info("sina[" + userMap.getSinaUserId() + "] から tqq[" + userMap.getTqqUserId() + "]へ同期化開始");

		// sinaのユーザIDを元にAccessTokenを取り出す
		AuthToken sinaAuthToken = authTokenDao.getByUserId(userMap.getSinaUserId());

		// AccessTokenでAPIオブジェクトを初期化する
		sinaWeiboApi = new com.appspot.piment.api.sina.WeiboApi(sinaAuthToken);

		// tqqのユーザIDを元にAccessTokenを取り出す
		AuthToken tqqAuthToken = authTokenDao.getByUserId(userMap.getTqqUserId());

		// AccessTokenでAPIオブジェクトを初期化する
		tqqWeiboApi = new com.appspot.piment.api.tqq.WeiboApi(tqqAuthToken);

		// sinaから前回の同期化以降対象ユーザが発表した新メッセージを取得する
		List<Status> userMessages = sinaWeiboApi.getUserTimeline(userMap);

		log.info("同期化件数：" + userMessages.size());

		// メッセージ単位で同期化処理を行う
		
		Status status = null;
		for (int i = userMessages.size() - 1; i >= 0 ; i--) {
		  status = userMessages.get(i);
		  try {

			log.info("sina[" + status.getId() + "]メッセージ同期化中...");

			// テキストメッセージなら
			if (StringUtils.isNotBlank(status.getText())) {
			  // 同じメッセージをtqqへ発表する
			  Response response = tqqWeiboApi.sendMessage(status.getText().trim(), null);
			  // 処理成功ならば、同期化レコードをデータストアへ保存する
			  if (Constants.TQQ_SUCCEED.equals(response.getErrcode()) && response.getData() != null) {
				WeiboMap weiboMap = new WeiboMap();
				weiboMap.setSinaWeiboId(String.valueOf(status.getId()));
				weiboMap.setTqqWeiboId(response.getData().getId());
				weiboMap.setUserMapId(userMap.getId());
				weiboMap.setCreateTime(DateUtils.getSysDate());
				weiboMap.setCreator(Job1001.class.getName());
				weiboMap.setUpdateTime(DateUtils.getSysDate());
				weiboMap.setUpdator(Job1001.class.getName());
				weiboMap = weiboMapDao.save(weiboMap);
				log.severe("同期化成功！！！");
				log.severe("メッセージID：" + status.getId());
				log.severe("同期化履歴レコードID：" + weiboMap.getId());
			  } else {
				String msg001 = "sina[" + status.getId() + "]メッセージをTQQへの送信が失敗しました。";
				log.severe(msg001);
				log.severe(response.toString());
				MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\nTQQからのレスポンス：\n" + response.toString() + "\n\n");
			  }
			}

			// TODO 画像ファイル
			// TODO ビデオ
			// TODO 引用

		  } catch (Exception e) {

			String msg001 = "同期化失敗しました、メッセージID：" + status.getId();
			log.severe(msg001);
			log.severe(e.getMessage());
			e.printStackTrace();
			MailUtils.sendErrorReport(msg001 + "\n\n処理メッセージ：" + status.toString() + "\n\n例外：\n" + JSON.encode(e, true));
			// 例外が起きても次ぎのメッセージの同期化を行う
		  }
		}
	  }

	  log.info("-- job1001 end --");

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}

  }
}
