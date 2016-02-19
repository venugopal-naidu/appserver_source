package com.vellkare.core

import grails.plugin.cache.Cacheable
import grails.transaction.Transactional

@Transactional
class AuthorityService {

  @Cacheable(value = "authorityNameCache", key = "#uName")
  def findByUName(String uName) {
    Authority.findByUName(uName)
  }
}
