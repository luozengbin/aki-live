package com.appspot.piment.dao;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
  private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

  private PMF() {
  }

  public static PersistenceManagerFactory get() {
	return pmfInstance;
  }

  public static <T> T saveEntity(T t) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  return pm.makePersistent(t);

	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public static <T> void saveEntity(T... t) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  for (T t2 : t) {
		pm.makePersistent(t2);
	  }
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public static <T> void saveEntity(List<T> t) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  for (T t2 : t) {
		pm.makePersistent(t2);
	  }
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public static <T> List<T> getAll(Class<T> clzss) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  @SuppressWarnings("unchecked")
	  List<T> result = (List<T>) pm.newQuery(clzss).execute();
	  pm.detachCopyAll(result);
	  return result;
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public static <T> void removeEntity(T t) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  pm.deletePersistent(t);
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

  public static <T> void removeEntity(List<T> t) {
	PersistenceManager pm = null;
	try {
	  pm = PMF.get().getPersistenceManager();
	  for (T t2 : t) {
		pm.deletePersistent(t2);
	  }
	} finally {
	  if (pm != null) {
		pm.close();
		pm = null;
	  }
	}
  }

}
