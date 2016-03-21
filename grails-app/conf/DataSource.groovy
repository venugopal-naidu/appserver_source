dataSource {
  pooled = true
  jmxExport = true
  driverClassName = "org.h2.Driver"
  username = "sa"
  password = ""
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
  cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
  singleSession = true // configure OSIV singleSession mode
  flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
  development {
    dataSource {
      dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
      //    url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
      url = "jdbc:mysql://localhost/vellkare"
      username = "root"
      password = "123456"
      driverClassName = "com.mysql.jdbc.Driver"
      logSql: true
      properties {
        maxActive = -1
        minEvictableIdleTimeMillis = 1800000
        timeBetweenEvictionRunsMillis = 1800000
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = true
        validationQuery = "SELECT 1"
        defaultAutoCommit = false
      }
    }
  }
  test {
    dataSource {
      dbCreate = "update"
      //url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
      url = "jdbc:mysql://localhost/vellkare_test"
      driverClassName = "com.mysql.jdbc.Driver"
      pooled = true
      username = "root"
      password = "123456"
      properties {
        maxActive = -1
        minEvictableIdleTimeMillis = 1800000
        timeBetweenEvictionRunsMillis = 1800000
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = true
        validationQuery = "SELECT 1"
        defaultAutoCommit = false
      }
    }
  }
  production {
    dataSource {
      dbCreate = "update"
      url = "jdbc:mysql://localhost/vellkare"
      driverClassName = "com.mysql.jdbc.Driver"
      pooled = true
      username = "root"
      password = "123456"
      properties {
        maxActive = -1
        minEvictableIdleTimeMillis = 1800000
        timeBetweenEvictionRunsMillis = 1800000
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = true
        validationQuery = "SELECT 1"
        defaultAutoCommit = false
      }
    }
  }
}
