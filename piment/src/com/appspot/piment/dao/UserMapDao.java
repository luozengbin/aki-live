package com.appspot.piment.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.UserMap;

public class UserMapDao {

  private PersistenceManager pm = null;

  public UserMapDao() {
	super();
  }

  public List<UserMap> getAllUserMaps() {

	List<UserMap> result = new ArrayList<UserMap>();
	try {

	  pm = PMF.get().getPersistenceManager();
	  @SuppressWarnings("unchecked")
	  List<UserMap> userMapList = (List<UserMap>) pm.newQuery(UserMap.class).execute();

	  for (UserMap userMap : userMapList) {
		result.add(userMap);
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
