package jp.o3co.config.jdbc

import javax.sql.DataSource

object ComboPooledDataSource {
  /**
   *
   */
  def apply(settings: JDBCSettings): DataSource = {
    val cpds = new com.mchange.v2.c3p0.ComboPooledDataSource()

    cpds.setJdbcUrl(settings.jdbcUrl)
    (settings.username, settings.password) match {
      case (Some(u), Some(p))  => 
        cpds.setUser(u)
        cpds.setPassword(p)
      case (Some(u), None) => 
        cpds.setUser(u)
      case _ => 
    }

    // Optional settings
    cpds.setMinPoolSize(settings.getOrElse[Int]("min-pool-size", 5))
    cpds.setAcquireIncrement(settings.getOrElse[Int]("acquire-increment", 5))
    cpds.setMaxPoolSize(settings.getOrElse[Int]("max-pool-size", 20))

    cpds
  }
}
