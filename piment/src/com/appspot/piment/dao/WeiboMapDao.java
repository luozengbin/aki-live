package com.appspot.piment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;

public class WeiboMapDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId";

  private static final String QL_002 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId && status == :status";

  private static final String QL_003 = "select from " + WeiboMap.class.getName() + " where sinaWeiboId == :sinaWeiboId";

  private PersistenceManager pm = null;

  public WeiboMapDao() {
	super();
  }

  public WeiboMap getNewestItem(Long userMapId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_001);
	  query.setOrdering("createTime desc");
	  query.setRange(0, 1);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.execute(userMapId);

	  return (weiboMapList != null && weiboMapList.size() > 0) ? weiboMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public WeiboMap getBySinaWeiboId(Long sinaWeiboId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_003);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.execute(sinaWeiboId);

	  return (weiboMapList != null && weiboMapList.size() > 0) ? weiboMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public List<WeiboMap> getFieldItem(Long userMapId) {

	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_002);
	  query.setOrdering("createTime asc");

	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("userMapId", userMapId);
	  params.put("status", WeiboStatus.FAILED);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.executeWithMap(params);

	  pm.detachCopyAll(weiboMapList);

	  return weiboMapList;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public WeiboMap save(WeiboMap weiboMap) {
	try {
	  pm = PMF.get().getPersistenceManager();

	  if (weiboMap.getCreateTime() == null) {
		weiboMap.setCreateTime(DateUtils.getSysDate());
	  }

	  if (StringUtils.isBlank(weiboMap.getCreator())) {
		weiboMap.setCreator(WeiboMapDao.class.getName());
	  }

	  weiboMap.setUpdateTime(DateUtils.getSysDate());
	  weiboMap.setUpdator(WeiboMapDao.class.getName());

	  weiboMap = pm.makePersistent(weiboMap);
	  return weiboMap;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

}
