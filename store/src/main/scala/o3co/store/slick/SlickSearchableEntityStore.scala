package o3co.store.slick

import o3co.search._
import o3co.store.entity._
import o3co.store.entity.Entity
import scala.concurrent.Future
import slick.lifted.ColumnOrdered
import slick.SearchExtension

trait SlickEntitySearch[I, E <: Entity[I], C] extends SearchExtension {
  this: SlickEntityStoreLike[I, E] =>

  import profile.api._

  import scala.language.implicitConversions

  def findAsync(condition: Option[C] = None, order: Option[OrderByFields] = None, size: Size, offset: Offset) = {
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

trait SlickSearchableEntityStoreLike[I, E <: Entity[I], C] extends SlickEntityStoreLike[I, E] with SlickEntitySearch[I, E, C] {
  this: EntityStore[I, E] =>
}
