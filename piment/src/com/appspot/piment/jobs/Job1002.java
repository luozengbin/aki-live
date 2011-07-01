package com.appspot.piment.jobs;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;

public class Job1002 extends HttpServlet {

  private static final long serialVersionUID = 8080410909511203717L;
  private static final Logger log = Logger.getLogger(Constants.FQCN + Job1002.class.getName());

  public Job1002() {
	super();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	try {

	  //InitDataDao initDataDao = new InitDataDao();

	  //initDataDao.initConfigItem();

	  //initDataDao.initAuthToken();

	  //initDataDao.initUserMap();

	} catch (Exception e) {
	  throw new RuntimeException(e);
	}

  }
}
