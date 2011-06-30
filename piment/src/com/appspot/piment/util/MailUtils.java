package com.appspot.piment.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.appspot.piment.Constants;
import com.appspot.piment.dao.ConfigItemDao;

public final class MailUtils {

  public static void sendErrorReport(String errorReport) {

	ConfigItemDao configItemDao = new ConfigItemDao();

	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	try {
	  Message msg = new MimeMessage(session);
	  InternetAddress adminAddress = new InternetAddress(configItemDao.getValue("app.admin.email.address"), configItemDao.getValue("app.admin.email.displayname"));
	  msg.setFrom(adminAddress);
	  msg.addRecipient(Message.RecipientType.TO, adminAddress);
	  msg.setSubject(Constants.APP_NAME + " Exception may occur !!!");
	  msg.setText(errorReport);
	  Transport.send(msg);
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

}
