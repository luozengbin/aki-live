package com.appspot.piment.util;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

  public static Date getSysDate() {
	return Calendar.getInstance().getTime();
  }

  public static Date getSysDate(int field, int amount) {
	Calendar cal = Calendar.getInstance();
	cal.add(field, amount);
	return cal.getTime();
  }
}
