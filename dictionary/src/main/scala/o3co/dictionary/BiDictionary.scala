package o3co.dictionary

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Await
import o3co.config.GlobalSettings

/**
 *
 */
trait BiDictionary[K, V] extends BaseDictionary[K, V] {
  def getKey(value: V): Option[K]

  def removeValue(value: V): Unit
}


/**
 *
 */
trait AsyncBiDictionary[K, V] extends BaseAsyncDictionary[K, V] {
  
  /**
   *
   */
  def getKeyAsync(value: V): Future[Option[K]]

  /**
   *
   */
  def removeValueAsync(value: V): Future[Unit]
}

object BiDictionary {

  /**
   *
   */
  implicit val timeout: FiniteDuration = GlobalSettings.defaults.timeout
  
  /**
   *
   */
  def apply[K, V](async: AsyncBiDictionary[K, V])(implicit timeout: FiniteDuration) = 
    new BiDictionary[K, V] {
      def getKey(value: V) = Await.result(async.getKeyAsync(value), timeout)

      def removeValue(value: V) = Await.result(async.removeValueAsync(value), timeout)

      def containsKey(key: K) = Await.result(async.containsKeyAsync(key), timeout)

      def containsValue(value: V) = Await.result(async.containsValueAsync(value), timeout)
      
      def get(key: K) = Await.result(async.getAsync(key), timeout)

      def put(key: K, value: V) = Await.result(async.putAsync(key, value), timeout)

      def remove(key: K) = Await.result(async.removeAsync(key), timeout)

      def keys = Await.result(async.keysAsync, timeout)

      def values = Await.result(async.valuesAsync, timeout)
    }
}
