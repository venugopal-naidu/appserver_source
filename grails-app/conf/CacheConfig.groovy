import grails.util.Environment

config = {
  defaults {
    eternal false
    overflowToDisk true
    maxElementsInMemory 10000
    maxElementsOnDisk 10000000
    timeToIdleSeconds 3600
    diskPersistent false
    statistics true
  }

  defaultCache {
    maxElementsInMemory 10000
    eternal false
    timeToIdleSeconds 120
    timeToLiveSeconds 120
    overflowToDisk true
    maxElementsOnDisk 10000000
    diskPersistent false
    diskExpiryThreadIntervalSeconds 120
    memoryStoreEvictionPolicy 'LRU'
    statistics true
  }

  ['authorityNameCache'].each { cacheName ->
    cache {
      name cacheName
      if (Environment.getCurrentEnvironment() == Environment.PRODUCTION) {
        timeToLiveSeconds 21600 // six hours
      } else {
        timeToLiveSeconds 60 // 1 minute
      }
    }
  }

}