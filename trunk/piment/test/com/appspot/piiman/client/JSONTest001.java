package com.appspot.piiman.client;

import java.io.UnsupportedEncodingException;

import com.appspot.piment.dao.InitDataDao;

public class JSONTest001 {

  /**
   * @param args
   * @throws UnsupportedEncodingException
   */
  public static void main(String[] args) throws UnsupportedEncodingException {

	InitDataDao initDataDao = new InitDataDao();
	initDataDao.initUserMap();
  }

}
