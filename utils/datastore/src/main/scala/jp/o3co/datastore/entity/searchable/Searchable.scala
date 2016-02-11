package jp.o3co.datastore
package entity
package search

import jp.o3co.search.FieldConditions
import jp.o3co.search.OrderBy
import scala.concurrent.Future

trait Searchable[K, E <: BaseEntity[K]] {
  this: EntityStore[K, E] => 

  /**
   * Search by fields condition
   */
  def searchEntitiesAsync(condition: Option[FieldConditions] = None, order: Option[OrderBy] = None, size: Int = 10, offset: Int = 0): Future[Seq[EntityKey]]
}
