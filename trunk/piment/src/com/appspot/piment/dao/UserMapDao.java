package com.appspot.piment.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.UserMap;
import com.appspot.piment.shared.StringUtils;

public class UserMapDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + UserMap.class.getName() + " where frequency == :frequency";
  private static final String QL_002 = "select from " + UserMap.class.getName() + " where sinaUserId == :sinaUserId";
  private static final String QL_003 = "select from " + UserMap.class.getName();

  private PersistenceManager pm = null;

  public UserMapDao() {
	super();
  }

  public List<UserMap> getAllUserMaps() {

	List<UserMap> result = new ArrayList<UserMap>();
	try {

	  pm = PMF.get().getPersistenceManager();

	  @SuppressWarnings("unchecked")
	  List<UserMap> userMapList = (List<UserMap>) pm.newQuery(QL_003).execute();

	  for (UserMap userMap : userMapList) {
		// 制御フラグより
		if (!userMap.isDisable() && StringUtils.isNotBlank(userMap.getSinaUserId()) && StringUtils.isNotBlank(userMap.getTqqUserId())) {
		  userMap.getFeatures().size();
		  userMap = pm.detachCopy(userMap);
		  result.add(userMap);
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

  public List<UserMap> getUserMaps(int frequency) {

	List<UserMap> result = new ArrayList<UserMap>();
	try {

	  pm = PMF.get().getPersistenceManager();

	  @SuppressWarnings("unchecked")
	  List<UserMap> userMapList = (List<UserMap>) pm.newQuery(QL_001).execute(frequency);

	  for (UserMap userMap : userMapList) {
		// 制御フラグより
		if (!userMap.isDisable() && StringUtils.isNotBlank(userMap.getSinaUserId()) && StringUtils.isNotBlank(userMap.getTqqUserId())) {
		  userMap.getFeatures().size();
		  userMap = pm.detachCopy(userMap);
		  result.add(userMap);
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

  public UserMap getUserMap(String sinaUserId) {

	UserMap result = null;
	try {

	  pm = PMF.get().getPersistenceManager();

	  @SuppressWarnings("unchecked")
	  List<UserMap> userMapList = (List<UserMap>) pm.newQuery(QL_002).execute(sinaUserId);

	  for (UserMap userMap : userMapList) {
		// 制御フラグより
		if (!userMap.isDisable() && StringUtils.isNotBlank(userMap.getSinaUserId()) && StringUtils.isNotBlank(userMap.getTqqUserId())) {
		  userMap.getFeatures().size();
		  userMap = pm.detachCopy(userMap);
		  result = userMap;
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
}
