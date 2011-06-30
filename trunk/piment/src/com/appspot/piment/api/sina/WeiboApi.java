package com.appspot.piment.api.sina;

import java.util.List;
import java.util.logging.Logger;

import weibo4j.Paging;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.WeiboMapDao;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());

  //private WeiboMapDao weiboMapDao = null;

  public WeiboApi() {
	super();
  }

  public WeiboApi(AuthToken authToken) {
	super(authToken);
  }

  @Override
  protected void subInit() {
	
  }

  /**
   * sinaのUserTimelineインタフェースへ問い合わせて、ユーザ発表したメッセージを取得する
   * 
   * @param userMap
   *          取得対象ユーザのID
   * @return メッセージリスト
   */
  public List<Status> getUserTimeline(UserMap userMap) {

	try {
	  // sinaへの問い合わせパラメータを初期化する
	  Paging paging = new Paging();
	  // 取得ページ数
	  paging.setPage(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.page")));
	  // 取得数
	  paging.setCount(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.count")));

	  // 前回同期化された最後の履歴レコードを取り出す
	  WeiboMapDao weiboMapDao = new WeiboMapDao();
	  WeiboMap lastestCreateWeiboMap = weiboMapDao.getNewestItem(userMap.getId());
	  if (lastestCreateWeiboMap != null) {
		// 最後処理されたメッセージのIDを「SinceId」条件として設定する
		paging.setSinceId(Long.valueOf(lastestCreateWeiboMap.getSinaWeiboId()));
	  }

	  log.info("sinaからメッセージIDが \"" + paging.getSinceId() + "\"以降のユーザメッセージを取得する");

	  // 問い合わせを行う
	  return this.weibo.getUserTimeline(paging);

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }

}
