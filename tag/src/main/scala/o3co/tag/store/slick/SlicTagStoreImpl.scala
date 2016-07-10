package o3co.tag
package store
package slick

import _root_.slick.driver.JdbcProfile
import o3co.store.slick.SlickStoreLike

/**
 */
trait SlickTagStoreImpl[O, T <: Tag[T]] extends TagStore[O, T] with SlickStoreLike {

  val profile: JdbcProfile
  import profile.api._
  import profile.api.{Tag => TableTag}

  implicit def ownerColumnType: ColumnType[O]
  implicit def tagColumnType: ColumnType[T]

  /**
   * Default table name
   */
  val tableName = "tags"

  /**
   * Table definitions
   */
  class Tags(tableTag: TableTag) extends Table[(O, T)](tableTag, tableName) {
    def owner   = column[O]("owner")
    def tag     = column[T]("tag")

    def  * = (owner, tag)
  }

  def database: Database 

  def tags = TableQuery[Tags]

  def tables = Seq(tags)

  /**
   *
   */
  def getTagsForAsync(owner: O) = {
    database.run(tags.filter(_.owner === owner).map(_.tag).result)
      .map(_.toSet)
  }

  /**
   *
   */
  def getOwnersForAsync(tag: T) = {
    database.run(tags.filter(_.tag === tag).map(_.owner).result)
      .map(_.toSet)
  }
}

