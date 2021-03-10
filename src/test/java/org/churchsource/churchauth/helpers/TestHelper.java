package org.churchsource.churchauth.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestHelper {
  public static Date getDate(String format, String date) {
    Date baptismDate = null;
    try {
      baptismDate = new SimpleDateFormat(format).parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return baptismDate;
  }
}
