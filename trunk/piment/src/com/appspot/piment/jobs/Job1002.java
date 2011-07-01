package com.appspot.piment.jobs;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.ConfigItemDao;
import com.appspot.piment.dao.PMF;
import com.appspot.piment.dao.UserMapDao;
import com.appspot.piment.model.UserMap;
import com.appspot.piment.util.DateUtils;

public class Job1002 extends HttpServlet {

  private static final long serialVersionUID = 8080410909511203717L;
  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1002.class.getName());

  public Job1002() {
	super();
	//ConfigItemDao configItemDao = new ConfigItemDao();
	//configItemDao.initData();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	try {

	  UserMap userMap = new UserMap();
	  userMap.setSinaUserId("11111");
	  userMap.setTqqUserId("222");
	  userMap.setDisable(true);
	  userMap.setCreateTime(DateUtils.getSysDate());
	  userMap.setCreator("dummy");
	  userMap.setUpdateTime(DateUtils.getSysDate());
	  userMap.setUpdator("dummy");
	  PMF.saveEntity(userMap);

	  List<UserMap> userMaps = (new UserMapDao()).getAllEnableUserMaps();
	  for (UserMap userMap1 : userMaps) {
		userMap1.setDisable(false);
		PMF.saveEntity(userMap1);
	  }

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}

  }
}
