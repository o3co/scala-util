package o3co.store
package vs 

import o3co.store.Store
import o3co.store.StoreLike
import scala.concurrent.Future

/**
 * ValueStore is a store type which is as a Set collection
 */
trait ReadOnlyValueStore[V] extends Store with ReadAccess[V]

trait ValueStore[V] extends ReadOnlyValueStore[V] with WriteAccess[V]

trait ReadOnlyValueStoreLike[V] extends StoreLike {
  this: ReadOnlyValueStore[V] =>

}

trait ValueStoreLike[V] extends StoreLike {
  this: ValueStore[V] => 
}


object ValueStore {

  /**
   *
   */
  trait Read[V] {
    this: Store => 

    /**
     */
    def existsAsync(value: V): Future[Boolean]

    /**
     */
    def valuesAsync(): Future[Seq[V]]
  }

  /**
   *
   */
  trait Write[V] {
    this: Store =>

    /**
     * Upsert values
     */
    def putAsync(values: V *): Future[Unit]

    /**
     * Add new values
     */
    def addAsync(values: V *): Future[Unit]

    /**
     *
     */
    def deleteAsync(values: V *): Future[Unit]
  }
}
