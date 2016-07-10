package jp.o3co.id.store
package impl

import scala.concurrent.ExecutionContext
import slick.driver.JdbcProfile

/**
 *
 */
trait SlickIDStoreImpl[ID] extends IDStoreImpl[ID] {
  
  val profile: JdbcProfile
  import profile.ColumnType
  import profile.api._

  def database: Database

  implicit def executionContext: ExecutionContext 

  implicit def idColumnType: ColumnType[ID]

  class Ids(tag: Tag) extends Table[(ID)](tag, "ids") {
    def id  = column[ID]("id", O.PrimaryKey)

    def * = (id)
  }

  val ids = TableQuery[Ids]

  def existsAsync(id: ID) = {
    database.run(ids.filter(_.id === id).length.result)
      .map(s => s > 0)
  }

  def putAsync(id: ID) = {
    database.run(ids += id)
      .map(_ => (): Unit)
  }

  def deleteAsync(id: ID) = {
    database.run(ids.filter(_.id === id).delete)
      .map(_ => (): Unit)
  }
}

