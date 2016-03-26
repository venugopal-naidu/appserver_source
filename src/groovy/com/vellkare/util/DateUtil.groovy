package com.vellkare.util

import java.text.SimpleDateFormat

/**
 * Created by roopesh on 19/03/16.
 */
class DateUtil {
  static final String DATE_FORMAT = "MM-dd-yyyy"
  static final String USER_DATE_FORMAT = "dd-MMMMM-yyyy"
  static final String USER_DATE_TIME_FORMAT = "dd-MMMMM-yyyy HH:mm:ss a"

  static SimpleDateFormat dfDate = new SimpleDateFormat(DATE_FORMAT)
  static SimpleDateFormat userDfDate = new SimpleDateFormat(USER_DATE_FORMAT)
  static SimpleDateFormat userDfDateTime = new SimpleDateFormat(USER_DATE_TIME_FORMAT)

  static final String TIME_FORMAT = "HH:mm:ss"
  static SimpleDateFormat dfTime = new SimpleDateFormat(TIME_FORMAT)

  static final String DATE_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss a"
  static SimpleDateFormat dTDate = new SimpleDateFormat(DATE_TIME_FORMAT)


  static String getPrintableDateString(Date date){
    dfDate.format(date)
  }

  static Date parseDateString(String date){
    dfDate.parse(date)
  }

  static Date parseDateStringUserFormat(String date){
    userDfDate.parse(date)
  }

  static Date parseDateTimeStringUserFormat(String date){
    userDfDateTime.parse(date)
  }

  static Date getDateTimeStringUserFormat(Date date){
    userDfDateTime.format(date)
  }

  static String getPrintableTimeString(Date date){
    dfTime.format(date)
  }

  static String getPrintableDateTimeString(Date date){
    dTDate.format(date)
  }

}
