package o3co.tag
package store
package dal

import slick.driver.JdbcProfile
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
    def tag     = column[T]("tag", O.Length(255))

    def  * = (owner, tag)

    def uniqueIdx = index("uniqueIdx", (owner, tag), unique = true)
  }

  def database: Database 

  def tags = TableQuery[Tags]

  def tables = Seq(tags)

  /**
   *
   */
  def getTagsAsync(owner: O) = {
    database.run(tags.filter(_.owner === owner).map(_.tag).result)
      .map(_.toSet)
  }

  /**
   *
   */
  def getOwnersAsync(tag: T) = {
    database.run(tags.filter(_.tag === tag).map(_.owner).result)
      .map(_.toSet)
  }

  /**
   *
   */
  def tagExistsAsync(owner: O, tag: T) = {
    database.run(tags.filter(r => r.owner === owner && r.tag === tag).size.result)
      .map (_ > 0)
  }

  /**
   *
   */
  def putTagAsync(owner: O, tag: T) = {
    database.run(tags.insertOrUpdate((owner, tag)))
      .map (_ => (): Unit)
  }

  /**
   */
  def putTagAsync(owner: O, tags: Set[T]) = {
    putTagSetAsync(tags.map(tag => (owner, tag)))
  }//: Future[Unit]

  /**
   *
   */
  def putTagSetAsync(tags: Set[(O, T)]) = {
    database.run(DBIO.sequence(tags.map(t => this.tags += t).toSeq))
      .map (_ => (): Unit)
  } //: Future[Unit] 

  /**
   *
   */
  def deleteTagAsync(owner: O, tag: T) = {
    database.run(this.tags.filter(r => r.owner === owner && r.tag === tag).delete)
      .map (_ => (): Unit)
  } //: Future[Unit]

  /**
   *
   */
  def deleteTagAsync(owner: O, tags: Set[T]) = {
    database.run(this.tags.filter(r => (r.owner === owner) && (r.tag inSet(tags))).delete)
      .map (_ => (): Unit)
  } //: Future[Unit]

  /**
   *
   */
  def deleteTagSetAsync(tags: Set[(O, T)]) = {
    database.run(
      DBIO.sequence(tags.map(t => this.tags.filter(r => r.owner === t._1 && r.tag === t._2).delete).toSeq)
    )
      .map (_ => (): Unit)
  } //: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(owner: O) = {
    database.run(tags.filter(r => r.owner === owner).delete)
      .map(_ => (): Unit)
  } //: Future[Unit]

  /**
   *
   */
  def deleteAllTagsAsync(tag: T) = {
    database.run(tags.filter(r => r.tag === tag).delete)
      .map(_ => (): Unit)
  } //: Future[Unit]

  /**
   *
   */
  def replaceAllTagsAsync(owner: O, tags: Set[T]) = {
    database.run((for {
      _ <- this.tags.filter(r => r.owner === owner).delete
      _ <- DBIO.sequence(tags.map(t => this.tags += (owner, t)).toSeq)
    } yield ()).transactionally)
  } //: Future[Unit] 

  def replaceTagsAsync(owner: O, newTags: Set[T], oldTags: Set[T]) = {
    database.run((for {
      _ <- this.tags.filter(r => r.owner === owner && (r.tag inSet(oldTags))).delete
      _ <- DBIO.sequence(newTags.map(t => this.tags += (owner, t)).toSeq)
    } yield()).transactionally)
  }
}

