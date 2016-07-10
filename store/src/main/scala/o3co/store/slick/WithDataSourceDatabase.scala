package o3co.store.slick

import javax.sql.DataSource

/**
 * Provide database using datasource
 */
trait WithDataSourceDatabase {
  this: SlickStoreLike => 

  import profile.api._
  def dataSource: DataSource

  lazy val database = Database.forDataSource(
    dataSource,
    executor = AsyncExecutor("organization", numThreads=10, queueSize=1000)
  )
}
