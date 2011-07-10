package com.appspot.piment.dao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.piment.Constants;
import com.appspot.piment.model.CommentMap;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;

public class CommentMapDao {

  private static final Logger log = Logger.getLogger(Constants.FQCN + CommentMapDao.class.getName());

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + CommentMap.class.getName() + " where userMapId == :userMapId";

  private static final String QL_002 = "select from " + CommentMap.class.getName() + " where userMapId == :userMapId && status == :status";

  // private static final String QL_003 = "select from " +
  // CommentMap.class.getName() + " where sinaWeiboId == :sinaWeiboId";

  private static final String QL_004 = "select from " + CommentMap.class.getName() + " where updateTime <= :before";

  private PersistenceManager pm = null;

  public CommentMapDao() {
	super();
  }

  public CommentMap getNewestItem(Long userMapId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_001);
	  query.setOrdering("createTime desc");
	  query.setRange(0, 1);

	  @SuppressWarnings("unchecked")
	  List<CommentMap> CommentMapList = (List<CommentMap>) query.execute(userMapId);

	  return (CommentMapList != null && CommentMapList.size() > 0) ? CommentMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  // public CommentMap getBySinaWeiboId(Long sinaWeiboId) {
  // try {
  // pm = PMF.get().getPersistenceManager();
  // Query query = pm.newQuery(QL_003);
  //
  // @SuppressWarnings("unchecked")
  // List<CommentMap> CommentMapList = (List<CommentMap>)
  // query.execute(sinaWeiboId);
  //
  // return (CommentMapList != null && CommentMapList.size() > 0) ?
  // CommentMapList.get(0) : null;
  //
  // } finally {
  // if (pm != null) {
  // pm.close();
  // pm = null;
  // }
  // }
  // }

  public List<CommentMap> getFieldItem(Long userMapId) {

	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_002);
	  query.setOrdering("createTime asc");

	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("userMapId", userMapId);
	  params.put("status", WeiboStatus.FAILED);

	  @SuppressWarnings("unchecked")
	  List<CommentMap> CommentMapList = (List<CommentMap>) query.executeWithMap(params);

	  pm.detachCopyAll(CommentMapList);

	  return CommentMapList;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public CommentMap save(CommentMap CommentMap) {
	try {
	  pm = PMF.get().getPersistenceManager();

	  if (CommentMap.getCreateTime() == null) {
		CommentMap.setCreateTime(DateUtils.getSysDate());
	  }

	  if (StringUtils.isBlank(CommentMap.getCreator())) {
		CommentMap.setCreator(CommentMapDao.class.getName());
	  }

	  CommentMap.setUpdateTime(DateUtils.getSysDate());
	  CommentMap.setUpdator(CommentMapDao.class.getName());

	  CommentMap = pm.makePersistent(CommentMap);
	  return CommentMap;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public void removeOlder(int dayBefore) {
	try {
	  pm = PMF.get().getPersistenceManager();

	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.DAY_OF_YEAR, -dayBefore);

	  @SuppressWarnings("unchecked")
	  List<CommentMap> CommentMapList = (List<CommentMap>) pm.newQuery(QL_004).execute(cal.getTime());
	  if (CommentMapList != null && CommentMapList.size() > 0) {
		for (CommentMap oldCommentMap : CommentMapList) {
		  log.info("コメント同期化履歴の削除：" + oldCommentMap.getId());
		  pm.deletePersistent(oldCommentMap);
		}
	  }

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

}
