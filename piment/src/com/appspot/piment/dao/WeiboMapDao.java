package com.appspot.piment.dao;

import java.util.ArrayList;
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

  private static final String QL_002 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId and status == :status";

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

  public List<WeiboMap> getFieldItem(Long userMapId) {

	List<WeiboMap> result = new ArrayList<WeiboMap>();

	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_002);
	  query.setOrdering("createTime asc");

	  Map<String, Object> params = new HashMap<String, Object>();
	  params.put("userMapId", userMapId);
	  params.put("status", WeiboStatus.FAILED);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.executeWithMap(params);

	  for (WeiboMap weiboMap : weiboMapList) {
		result.add(weiboMap);
	  }

	  return result;

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
		weiboMap.setCreateTime(DateUtils.getSysDate());
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
