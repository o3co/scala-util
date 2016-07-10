package jp.o3co.tag.dictionary
package store

import java.sql.Timestamp
import java.util.Date
import jp.o3co.tag.TagName 
import jp.o3co.tag.TagLabel 
import scala.concurrent.ExecutionContext
import slick.driver.JdbcProfile

trait SlickSQL extends BaseTagDictionaryStore {

  val profile: JdbcProfile 

  import profile.api._

  def settings: TagDictionaryStoreSettings

  lazy val database: Database = Database.forDataSource(settings.dataSource)

  implicit def executionContext: ExecutionContext

  implicit def tagSegmentColumnType = MappedColumnType.base[TagSegment, String](s => s.toString, s => NamedTagSegment(s))
  implicit def tagNameColumnType   = MappedColumnType.base[TagName, String](l => l.toString, s => TagName(s)) 
  implicit def tagLabelColumnType   = MappedColumnType.base[TagLabel, String](l => l.toString, s => TagLabel(s)) 
  implicit def dateColumnType = MappedColumnType.base[Date, Timestamp](d => new Timestamp(d.getTime()), t => new Date(t.getTime()))
  
  class Terms(tag: Tag) extends Table[TermEntity](tag, "terms") {
    val key     = column[TagName]("key", O.SqlType("varchar(255)"))
    val segment = column[TagSegment]("segment", O.SqlType("varchar(255)"))
    val label   = column[TagLabel]("label", O.SqlType("varchar(255)"))
    // For SearchIndexer 
    val created = column[Date]("created_at")
    val updated = column[Date]("updated_at")

    def * = (key, segment, label, created, updated) <> (TermEntity.tupled, TermEntity.unapply)

    def pk               = primaryKey("terms_pk", (key, segment))
    def segmentIdx = index("segmentIdx", (segment))
  }

  def terms = TableQuery[Terms] 

  def containsAsync(key: TermPK) = {
    containsLabelKey(key._1, Option(key._2))
  }

  def containsLabelKey(key: TagName, segment: Option[TagSegment]) = {
    database.run((segment match {
      case Some(s) => terms.filter(t => t.key === key && t.segment === s)
      case None    => terms.filter(t => t.key === key)
    }).length.result)
      .map(_ > 0)
  }

  def getAsync(key: TermPK) = {
    database.run(terms.filter(t => t.key === key._1 && t.segment === key._2).result.headOption)
  }

  def putAsync(key: TermPK, entity: TermEntity) = {
    database.run(terms.filter(t => t.key === key._1 && t.segment === key._2).result.headOption)
      .map {
        case Some(e) => 
          (Option(e), e.update(label = entity.label))
        case None         =>
          (None, entity)
      }
      .flatMap { 
        case (prev, e) =>  
          database.run(terms.insertOrUpdate(e))
            .map(_ => prev)
      }
  }
  
  def deleteAsync(key: TermPK) = {
    val q = terms.filter(t => t.key === key._1 && t.segment === key._2)
    database.run(DBIO.sequence(
      Seq(q.result.headOption, q.delete)
    )).map(ret => ret.head.asInstanceOf[Option[TermEntity]])

  }

  def getLabel(key: TagName, segment: TagSegment) = {
    database.run(terms.filter(t => (t.key === key) && (t.segment === segment)).map(_.label).result.headOption)
  }

  def getLabelKeys(label: TagLabel, segment: TagSegment) = {
    database.run(terms.filter(t => (t.label === label) && (t.segment === segment)).map(_.key).result)
  }

  def putLabel(key: TagName, segment: TagSegment, label: TagLabel) = {
    putAsync((key, segment), TermEntity(key, segment, label))
      .map(prev => prev.map(_.label))
  }

  def deleteLabel(key: TagName, segment: TagSegment) = {
    deleteAsync((key, segment)).map { entity => 
      entity.map(_.label)
    }
  }
}
