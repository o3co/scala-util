package o3co.store
package vs 

import scala.concurrent.Future

/**
 */
object ValueStore {

  /**
   *
   */
  trait Read[V] extends Store.Access {
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
  trait Write[V] extends Store.Access {
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

  type Full[V] = Read[V] with Write[V]
}

/**
 */
trait ValueStore[V] extends Store with ValueStore.Read[V] with ValueStore.Write[V] {

}

/**
 */
trait ReadOnlyValueStore[V] extends Store with ValueStore.Read[V] {
}
