package com.vellkare.util

import java.text.SimpleDateFormat

/**
 * Created by roopesh on 19/03/16.
 */
class DateUtil {
  static final String DATE_FORMAT = "MM-dd-yyyy"
  static SimpleDateFormat dfDate = new SimpleDateFormat(DATE_FORMAT)

  static final String TIME_FORMAT = "HH:mm:ss"
  static SimpleDateFormat dfTime = new SimpleDateFormat(TIME_FORMAT)

  static final String DATE_TIME_FORMAT = "MM-dd-yyyy HH:mm:ss a"
  static SimpleDateFormat dTDate = new SimpleDateFormat(DATE_TIME_FORMAT)


  static String getPrintableDateString(Date date){
    dfDate.format(date)
  }

  static String getPrintableTimeString(Date date){
    dfTime.format(date)
  }

  static String getPrintableDateTimeString(Date date){
    dTDate.format(date)
  }

}
