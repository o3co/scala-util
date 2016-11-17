package slick

import javax.sql.DataSource

/**
 * Database specified by DataSource 
 */
trait DataSourceDatabase extends WithJDBC {

  import profile.api._

  def dataSource: DataSource

  /**
   * Override executor if you want to change the behavior
   */
  val executor = AsyncExecutor("database.default_executor", numThreads = 10, queueSize = 1000)

  /**
   * create database from datasource 
   */
  lazy val database = Database.forDataSource(
    dataSource,
    executor = executor
  )
}

object DataSourceDatabase {

  /**
   *
   */
  trait WithConfig extends DataSourceDatabase {
    this: WithJDBC =>

    def config: com.typesafe.config.Config

    def datasourceConfig: com.typesafe.config.Config = config

    val dataSourceFactory: o3co.datasource.DataSourceFactory = o3co.datasource.ComboPooledDataSourceFactory

    lazy val dataSource = dataSourceFactory(datasourceConfig)
  }
}
