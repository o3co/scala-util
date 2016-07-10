package com.mchange.v2.c3p0

import javax.sql.DataSource
import o3co.config.JDBCSettingsLike
import o3co.config.Settings

object ComboPooledDataSourceFactory {
  
  def apply(settings: Settings with JDBCSettingsLike): DataSource = {
    val cpds = new ComboPooledDataSource()

    cpds.setJdbcUrl(settings.jdbcUrl)
    (settings.username, settings.password) match {
      case (Some(u), Some(p))  => 
        cpds.setUser(u)
        cpds.setPassword(p)
      case (Some(u), None) => 
        cpds.setUser(u)
      case _ => 
    }

    import com.typesafe.config.ImplicitConfigExtensions._
    // Optional settings
    cpds.setMinPoolSize(settings.config.getOrElse[Int]("min-pool-size", 5))
    cpds.setAcquireIncrement(settings.config.getOrElse[Int]("acquire-increment", 5))
    cpds.setMaxPoolSize(settings.config.getOrElse[Int]("max-pool-size", 20))

    cpds
  }
}
