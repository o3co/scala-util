package o3co.dictionary

import scala.concurrent.Future

trait BiDictionaryLike[K, V] {
  this: BaseDictionary[K, V] => 
  
  def getKey(value: V): Option[K]

  def removeValue(value: V): Unit
}

trait BiDictionary[K, V] extends BaseDictionary[K, V] with BiDictionaryLike[K, V]


/**
 *
 */
trait AsyncBiDictionaryLike[K, V] {
  this: BaseAsyncDictionary[K, V] =>
  
  def getKeyAsync(value: V): Future[Option[K]]

  def removeValueAsync(value: V): Future[Unit]
}

trait AsyncBiDictionary[K, V] extends BaseAsyncDictionary[K, V] with AsyncBiDictionaryLike[K, V]
