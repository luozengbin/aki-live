package com.appspot.piment.api.sina;

import java.util.List;
import java.util.logging.Logger;

import weibo4j.Paging;
import weibo4j.Status;

import com.appspot.piment.Constants;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.shared.StringUtils;

public class WeiboApi extends ApiBase {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboApi.class.getName());

  public WeiboApi() {
	super();
	this.subInit();
  }

  public WeiboApi(AuthToken authToken) {
	super(authToken);
	this.subInit();
  }

  protected void subInit() {
  }

  /**
   * sinaのUserTimelineインタフェースへ問い合わせて、ユーザ発表したメッセージを取得する
   * 
   * @param sinceId
   *          取得条件開始位置のメッセージ
   * @param sinceId
   *          取得条件終了位置のメッセージ
   * 
   * @return メッセージリスト
   */
  public List<Status> getUserTimeline(String sinceId, String maxId) {

	try {
	  // sinaへの問い合わせパラメータを初期化する
	  Paging paging = new Paging();
	  // 取得ページ数
	  paging.setPage(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.page")));
	  // 取得数
	  paging.setCount(Integer.valueOf(this.configMap.get("sina.usertimeline.paging.count")));

	  // 取得範囲の開始位置の指定
	  if (StringUtils.isNotBlank(sinceId)) {
		paging.setSinceId(Long.valueOf(sinceId));
	  }

	  // 取得範囲の終了位置の指定
	  if (StringUtils.isNotBlank(maxId)) {
		paging.setMaxId(Long.valueOf(maxId));
	  }

	  log.info("sinaからメッセージIDが \"" + paging.getSinceId() + "\"以降のユーザメッセージを取得する");

	  // 問い合わせを行う
	  return this.weibo.getUserTimeline(paging);

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }
}
