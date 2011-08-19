package com.appspot.piment.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.piment.Constants;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.model.WeiboMap;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.model.WeiboStatus;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;

public class WeiboMapDao {

  private static final Logger log = Logger.getLogger(Constants.FQCN + WeiboMapDao.class.getName());

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId && source == :source";

  private static final String QL_002 = "select from " + WeiboMap.class.getName() + " where userMapId == :userMapId && status == :status";

  private static final String QL_003 = "select from " + WeiboMap.class.getName() + " where sinaWeiboId == :sinaWeiboId && userMapId == :userMapId";

  private static final String QL_004 = "select from " + WeiboMap.class.getName() + " where updateTime <= :before";

  private static final String QL_005 = "select from " + WeiboMap.class.getName() + " where tqqWeiboId == :tqqWeiboId";

  private PersistenceManager pm = null;

  public WeiboMapDao() {
	super();
  }

  public WeiboMap getNewestItem(Long userMapId, WeiboSource source) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_001);
	  query.setOrdering("createTime desc");
	  query.setRange(0, 1);
	  

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.executeWithArray(userMapId, source);

	  return (weiboMapList != null && weiboMapList.size() > 0) ? weiboMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public WeiboMap getBySinaWeiboId(Long sinaWeiboId, Long userMapId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_003);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.executeWithArray(sinaWeiboId, userMapId);

	  return (weiboMapList != null && weiboMapList.size() > 0 && weiboMapList.get(0).getStatus().equals(WeiboStatus.SUCCESSED)) ? weiboMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public WeiboMap getByTqqWeiboId(Long tqqWeiboId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  Query query = pm.newQuery(QL_005);

	  @SuppressWarnings("unchecked")
	  List<WeiboMap> weiboMapList = (List<WeiboMap>) query.execute(tqqWeiboId);

	  return (weiboMapList != null && weiboMapList.size() > 0) ? weiboMapList.get(0) : null;

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public List<WeiboMap> getFieldItem(Long userMapId, WeiboSource source) {

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
		if(weiboMap.getSource().equals(source)){
		  weiboMap = pm.detachCopy(weiboMap);
		  result.add(weiboMap);
		}
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

  @SuppressWarnings("unchecked")
  public void removeOlder(List<UserMap> userMaps, int dayBefore) {
	try {
	  pm = PMF.get().getPersistenceManager();

	  Query query = pm.newQuery(QL_001);
	  query.setOrdering("createTime desc");
	  query.setRange(0, 1);

	  List<WeiboMap> keepWeiboMaps = new ArrayList<WeiboMap>();
	  for (UserMap userMap : userMaps) {
		List<WeiboMap> weiboMapList = (List<WeiboMap>) query.execute(userMap.getId());
		if (weiboMapList != null && weiboMapList.size() > 0) {
		  keepWeiboMaps.add(weiboMapList.get(0));
		}
	  }

	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.DAY_OF_YEAR, -dayBefore);

	  List<WeiboMap> weiboMapList = (List<WeiboMap>) pm.newQuery(QL_004).execute(cal.getTime());
	  if (weiboMapList != null && weiboMapList.size() > 0) {
		for (WeiboMap oldWeiboMap : weiboMapList) {

		  boolean keepIt = false;
		  for (WeiboMap keepWeiboMap : keepWeiboMaps) {
			if (keepWeiboMap.getId().equals(oldWeiboMap.getId())) {
			  keepWeiboMaps.remove(keepWeiboMap);
			  keepIt = true;
			  log.info("同期化履歴の削除をスキップする：" + oldWeiboMap.getId());
			  break;
			}
		  }

		  if (!keepIt) {
			log.info("同期化履歴の削除：" + oldWeiboMap.getId());
			pm.deletePersistent(oldWeiboMap);
		  }
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
