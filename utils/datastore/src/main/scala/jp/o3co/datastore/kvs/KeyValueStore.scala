package jp.o3co.datastore
package kvs

import scala.concurrent.Future

/**
 * KeyValueStore 
 */
trait KeyValueStore[K, V] extends KeyValueStoreComponents[K, V] {

  /**
   *
   */
  def containsAsync(key: Key): Future[Boolean]

  /**
   * @return Value if exists, None otherwise
   */
  def getAsync(key: Key): Future[Option[Value]]

  /**
   * @return Previous value related with Key
   */
  def putAsync(key: Key, value: Value): Future[Option[Value]]

  /**
   * @return Deleted value 
   */
  def deleteAsync(key: Key): Future[Option[Value]]
}

