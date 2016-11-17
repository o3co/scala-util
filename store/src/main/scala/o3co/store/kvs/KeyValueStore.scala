package o3co.store
package kvs

import scala.concurrent.Future
import scala.concurrent.ExecutionContext


object KeyValueStore {

  trait Read[K, V] extends Store.Access {
    this: Store => 

    implicit def executionContext: ExecutionContext

    def existsAsync(key: K): Future[Boolean]

    def keysAsync: Future[Set[K]]
    
    def getAsync(keys: Set[K] = Set()): Future[Map[K, V]]

    /**
     * Get in order
     */
    def getAsync(keys: K *): Future[Seq[V]]

    def getAsync(key: K): Future[Option[V]] = getAsync(Set(key)).map(_.get(key))
  }


  trait Write[K, V] extends Store.Access {
    this: Store => 

    /**
     */
    def setAsync(key: K, value: V): Future[Unit] = setAsync(Map(key -> value))

    def setAsync(kvs: Map[K, V]): Future[Unit]

    /**
     *
     */
    def addAsync(key: K, value: V): Future[Unit] = addAsync(Map(key -> value))

    def addAsync(kvs: Map[K, V]): Future[Unit]

    def deleteAsync(keys: K *): Future[Unit]
  }

  type Full[K, V] = Read[K, V] with Write[K, V]
}

trait KeyValueStore[K, V] extends Store with KeyValueStore.Read[K, V] with KeyValueStore.Write[K, V]

trait ReadOnlyKeyValueStore[K, V] extends Store with KeyValueStore.Read[K, V]
