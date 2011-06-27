package com.appspot.piment.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.piment.model.WeiboMap;

public class WeiboMapDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId";

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

}
