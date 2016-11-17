package o3co.store.entity

import o3co.dal.SlickDAL
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.lifted.ColumnOrdered

/**
 * Base trait of EntityStore on Slick.
 */
trait SlickDALEntityStore[K, E <: Entity[K]] extends EntityStore[K, E] with SlickDAL {

  import profile.api._

  implicit def executionContext: ExecutionContext

  type Entities <: Table[E]

  def entities: TableQuery[Entities]

  def countAsync = {
    database.run(entities.length.result)
      .map(_.toLong)
  }

  def addAsync(entities: E *) = {
    database.run(
      if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities += e))
      else this.entities += entities.head
    )
      .map(_ => (): Unit)
  }

  //def addAsync(entity: E) = {
  //  database.run(
  //    entities += entity
  //  )
  //    .map(_ => (): Unit)
  //}

  def putAsync(entities: E *) = {
    database.run(
      if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities.insertOrUpdate(e)))
      else this.entities.insertOrUpdate(entities.head)
    )
      .map(_ => (): Unit)
  }

  //def putAsync(entity: E) = {
  //  database.run(
  //    entities.insertOrUpdate(entity) 
  //  )
  //    .map(_ => (): Unit)
  //}

  def valuesAsync() = {
    database.run(entities.result)
  }
}

object SlickDALEntityStore {

  /**
   * SlickDALEntityStoreWithPrimaryKey  
   * {{{
   *   trait UserStore extends SlickDALEntityStore.WithPK[UserID, User] with AuthDAL {
   *
   *     type Entities = Users
   *     val entities  = users
   *    
   *     // something...
   *   }
   * }}}
   */
  trait WithPK[K, E <: Entity[K]] extends SlickDALEntityStore[K, E] {
    this: SlickDAL =>
  
    import profile.api._
    import scala.language.reflectiveCalls
  
    type WithPrimaryKey = { def id: slick.lifted.Rep[K] }
    type Entities <: Table[E] with WithPrimaryKey 
  
    implicit def idColumnType: ColumnType[K]
  
    /**
     *
     */
    def existsAsync(id: K) = {
      database.run(entities.filter(_.id === id).size.result)
        .map (_ > 0)
    }
  
    def keysAsync() = {
      database.run(entities.map(_.id).result)
        .map(_.toSet)
    }

    def idsAsync() = keysAsync
  
    override def getAsync(id: K) = {
      database.run(entities.filter(_.id === id).result.headOption)
    }
  
    def getAsync(ids: K *) = {
      val order = ids.zipWithIndex.toMap
      database.run(entities.filter(_.id inSet ids.toSet).result)
        .map { es => es.sortBy(e => order(e.id)) }
    }
  
    def getAsync(ids: Set[K]) = {
      database.run(
        entities
          .filter(_.id inSet ids)
          .result
      ).map(_.map(e => (e.id, e)).toMap)
    }
  
    def deleteByIdAsync(ids: K *) = {
      database.run(
        if(ids.size > 1) DBIO.sequence(ids.map(id => entities.filter(_.id === id).delete))
        else this.entities.filter(_.id === ids.head).delete
      )
        .map(_ => (): Unit)
    }

    def deleteAsync(entities: E *) = {
      deleteByIdAsync(entities.map(_.id): _*) 
    }
  }

  /**
   * Searchable Behavior with o3co.search
   */
  trait Searchable[K, E <: Entity[K], C] extends slick.SearchExtension {
    this: WithPK[K, E] => 

    import o3co.search._
    import profile.api._

    import scala.language.implicitConversions

    def findAsync(condition: Option[C] = None, order: Option[OrderByFields] = None, size: Size = All, offset: Offset = 0): Future[(Seq[K], Long)] = {
      val query = (condition, order) match {
        case (Some(c), Some(o))  => entities.filter(c).sorted(o)
        case (Some(c), None)     => entities.filter(c)
        case (None, Some(o))     => entities.sorted(o)
        case (None, None)        => entities
      }
      
      database.run((for {
        hits <- query.length.result
        ids  <- size match {
          case All => query.map(_.id).drop(offset).result
          case Limit(s) => query.map(_.id).drop(offset).take(s).result
        }
      } yield(ids, hits.toLong)).transactionally)
    }
    
    implicit def conditionalFilter(condition: C): Entities => Rep[Option[Boolean]]

    implicit def fieldOrder(row: Entities): OrderByField => Option[ColumnOrdered[_]]
  }
}
