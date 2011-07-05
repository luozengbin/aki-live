package com.appspot.piment.dao;

import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;
import com.appspot.piment.shared.StringUtils;
import com.appspot.piment.util.DateUtils;

public class AuthTokenDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + AuthToken.class.getName() + " where token == :token";
  private static final String QL_002 = "select from " + AuthToken.class.getName() + " where userName == :userName && type == :type";
  private static final String QL_003 = "select from " + AuthToken.class.getName() + " where userName == null && createTime <= :before";

  private PersistenceManager pm = null;

  public AuthTokenDao() {
	super();
  }

  public AuthToken getByToken(String token) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  @SuppressWarnings("unchecked")
	  List<AuthToken> authTokenList = (List<AuthToken>) pm.newQuery(QL_001).execute(token);

	  return (authTokenList != null && authTokenList.size() > 0) ? authTokenList.get(0) : null;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public AuthToken getByUserId(String userId, WeiboSource source) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  @SuppressWarnings("unchecked")
	  List<AuthToken> authTokenList = (List<AuthToken>) pm.newQuery(QL_002).executeWithArray(userId, source);

	  return (authTokenList != null && authTokenList.size() > 0) ? authTokenList.get(0) : null;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public void clearTempToken(int beforeHours) {
	try {
	  pm = PMF.get().getPersistenceManager();

	  Calendar cal = Calendar.getInstance();
	  cal.add(Calendar.HOUR, -beforeHours);

	  @SuppressWarnings("unchecked")
	  List<AuthToken> authTokenList = (List<AuthToken>) pm.newQuery(QL_003).execute(cal.getTime());
	  if (authTokenList != null && authTokenList.size() > 0) {
		for (AuthToken oldAuthToken : authTokenList) {
		  pm.deletePersistent(oldAuthToken);
		}
	  }

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}

  }

  public AuthToken save(AuthToken authToken) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  if (StringUtils.isNotBlank(authToken.getUserName())) {
		@SuppressWarnings("unchecked")
		List<AuthToken> authTokenList = (List<AuthToken>) pm.newQuery(QL_002).executeWithArray(authToken.getUserName(), authToken.getType());
		if (authTokenList != null && authTokenList.size() > 0) {
		  for (AuthToken oldAuthToken : authTokenList) {
			pm.deletePersistent(oldAuthToken);
		  }
		}
	  }
	  authToken.setCreateTime(DateUtils.getSysDate());
	  authToken = pm.makePersistent(authToken);
	  return authToken;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

}
