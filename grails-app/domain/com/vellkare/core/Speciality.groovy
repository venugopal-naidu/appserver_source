package com.vellkare.core

/**
 * Created by roopesh on 07/03/16.
 */
class Speciality implements Serializable {
  String name
  boolean velkareVerified = false
  static mapping  = {
    cache true
  }
}
