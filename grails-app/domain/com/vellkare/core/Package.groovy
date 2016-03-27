package com.vellkare.core

class Package implements Serializable {

  String name

  Double cost = 0
  static constraints = {
  }
  static mapping = {
    cache true
  }
}
