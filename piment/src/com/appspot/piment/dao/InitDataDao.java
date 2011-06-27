package com.appspot.piment.dao;

import javax.jdo.PersistenceManager;

import com.appspot.piment.model.UserMap;
import com.appspot.piment.util.DateUtils;

public class InitDataDao {

  private PersistenceManager pm = null;

  public InitDataDao() {
    super();
  }

  public void initUserMap() {
    try {
      pm = PMF.get().getPersistenceManager();
      UserMap userMap = new UserMap("", "");
      userMap.setCreateTime(DateUtils.getSysDate());
      userMap.setCreator(InitDataDao.class.getName());
      userMap.setUpdateTime(DateUtils.getSysDate());
      userMap.setUpdator(InitDataDao.class.getName());
      
      pm.makePersistent(userMap);

    } finally {
      if (pm != null) {
        pm.close();
        pm = null;
      }
    }
  }
}
