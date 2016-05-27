package jp.o3co.datastore
package kvs

import akka.actor.ActorSelection
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext

trait KeyValueStoreAdapterLike[K, V] extends KeyValueStore[K, V] {

  val protocol: KeyValueStoreProtocolLike[Key, Value]

  import protocol._

  def endpoint: ActorSelection

  implicit def timeout: Timeout 

  implicit def executionContext: ExecutionContext

  /**
   *
   */
  def containsAsync(key: Key) = {
    (endpoint ? Contains(key))
      .map {
        case ContainsSuccess(exists) => exists
        case ContainsFailure(cause) => throw cause 
      }
  }

  /**
   * @return Value if exists, None otherwise
   */
  def getAsync(key: Key) = {
    (endpoint ? Get(key))
      .map {
        case GetSuccess(exists) => exists
        case GetFailure(cause) => throw cause 
      }
  }

  /**
   * @return Previous value related with Key
   */
  def putAsync(key: Key, value: Value) = {
    (endpoint ? Put(key, value))
      .map {
        case PutSuccess(exists) => exists
        case PutFailure(cause) => throw cause 
      }
  }

  /**
   * @return Deleted value 
   */
  def deleteAsync(key: Key) = {
    (endpoint ? Delete(key))
      .map {
        case DeleteSuccess(exists) => exists
        case DeleteFailure(cause) => throw cause 
      }
  }
}
