package com.appspot.piment.servlet;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.InitDataDao;

public class InitStoreData extends HttpServlet {

  private static final long serialVersionUID = 6662180842235417313L;

  private static final Logger log = Logger.getLogger(Constants.FQCN + InitStoreData.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp) {

	log.info("InitStoreData -- start");
	
	InitDataDao initDataDao = new InitDataDao();

	initDataDao.initConfigItem();

	initDataDao.initJobStatus();

	initDataDao.initAuthToken();

	initDataDao.initUserMap();

	log.info("InitStoreData -- end");
  }

}
