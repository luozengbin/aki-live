package com.appspot.piment.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.AuthToken;

public class AuthTokenDao {

  /**
   * DEFINATION OF QUERY
   */
  private static final String QL_001 = "select from " + AuthToken.class.getName() + " where token == :token";
  private static final String QL_002 = "select from " + AuthToken.class.getName() + " where userName == :userName";

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

  public AuthToken getByUserId(String userId) {
	try {
	  pm = PMF.get().getPersistenceManager();
	  @SuppressWarnings("unchecked")
	  List<AuthToken> authTokenList = (List<AuthToken>) pm.newQuery(QL_002).execute(userId);

	  return (authTokenList != null && authTokenList.size() > 0) ? authTokenList.get(0) : null;
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
