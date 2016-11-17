package o3co.store
package entity

import o3co.dal.SlickDAL
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.lifted.ColumnOrdered
import scala.language.reflectiveCalls

object SlickDALEntityStore {

  trait Base[K, E <: Entity[K]] extends Store {
    this: SlickDAL =>

    import profile.api._

    implicit def executionContext: ExecutionContext

    type Entities <: Table[E]

    def entities: TableQuery[Entities]

    def countAsync = {
      database.run(entities.length.result)
        .map(_.toLong)
    }
  }

  trait Read[K, E <: Entity[K]] extends Base[K, E] with EntityStore.Read[K, E] {
    this: SlickDAL => 

    import profile.api._
  }

  trait Write[K, E <: Entity[K]] extends Base[K, E] with EntityStore.Write[K, E] {
    this: SlickDAL =>

    import profile.api._

    def addAsync(entities: E *) = {
      database.run(
        if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities += e))
        else this.entities += entities.head
      )
        .map(_ => (): Unit)
    }

    def putAsync(entities: E *) = {
      database.run(
        if(entities.size > 1) DBIO.sequence(entities.map(e => this.entities.insertOrUpdate(e)))
        else this.entities.insertOrUpdate(entities.head)
      )
        .map(_ => (): Unit)
    }

    def valuesAsync() = {
      database.run(entities.result)
    }
  }

  type Full[K, E <: Entity[K]] = Read[K, E] with Write[K, E]

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
  trait WithPK[K, E <: Entity[K]] extends Base[K, E] {
    this: SlickDAL =>
  
    import profile.api._
    import scala.language.reflectiveCalls
  
    type WithPrimaryKey = { def id: slick.lifted.Rep[K] }
    type Entities <: Table[E] with WithPrimaryKey 
  
    implicit def idColumnType: ColumnType[K]
  }

  trait ReadWithPK[K, E <: Entity[K]] extends WithPK[K, E] with Read[K, E] {
    this: SlickDAL =>
    import profile.api._
  
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

  }
  
  trait WriteWithPK[K, E <: Entity[K]] extends WithPK[K, E] with Write[K, E] {
    this: SlickDAL =>
    import profile.api._

    def deleteByIdAsync(ids: K *) = {
      database.run(
        if(ids.size > 1) DBIO.sequence(ids.map(id => entities.filter(_.id === id).delete))
        else this.entities.filter(_.id === ids.head).delete
      )
        .map(_ => (): Unit)
    }

    override def deleteAsync(entities: E *) = {
      deleteByIdAsync(entities.map(_.id): _*) 
    }
  }

  /**
   * Searchable Behavior with o3co.search
   */
  trait Searchable[K, E <: Entity[K], C] extends EntityStore.Searchable[K, E, C] with slick.SearchExtension {
    this: SlickDAL with ReadWithPK[K, E] =>

    import o3co.search._
    import profile.api._

    import scala.language.implicitConversions

    def findAsync(condition: Option[C] = None, order: Option[OrderByFields] = None, size: Size = All, offset: Offset = 0): Future[(Seq[K], Long)] = {
      val query = (condition, order) match {
        case (Some(c), Some(o))  => entities.filter(c).sorted(o)
        case (Some(c), None)     => entities.filter(c).sorted(defaultOrder)
        case (None, Some(o))     => entities.sorted(o)
        case (None, None)        => entities.sorted(defaultOrder)
      }
      
      database.run((for {
        hits <- query.length.result
        ids  <- size match {
          case All => query.map(_.id).drop(offset).result
          case Limit(s) => query.map(_.id).drop(offset).take(s).result
        }
      } yield(ids, hits.toLong)).transactionally)
    }

    def defaultOrder: Entities => ColumnOrdered[_] = _.id.asc
    
    implicit def conditionalFilter(condition: C): Entities => Rep[Option[Boolean]]

    implicit def fieldOrder(row: Entities): OrderByField => Option[ColumnOrdered[_]]
  }

  type FullWithPK[K, E <: Entity[K]] = ReadWithPK[K, E] with WriteWithPK[K, E]
}

/**
 * Base trait of EntityStore on Slick.
 */
trait SlickDALEntityStore[K, E <: Entity[K]] extends EntityStore[K, E]
  with SlickDALEntityStore.ReadWithPK[K, E]
  with SlickDALEntityStore.WriteWithPK[K, E]
{
  this: SlickDAL =>  
}
