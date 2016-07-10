package o3co.dictionary

import scala.concurrent.Future

/**
 *
 */
trait DictionaryLike[K, V] {
  /**
   * Find keys for value 
   */
  def findKeys(value: V): Set[K]
}

trait Dictionary[K, V] extends BaseDictionary[K, V] with DictionaryLike[K, V]

/**
 *
 */
trait AsyncDictionaryLike[K, V] {
  
  def findKeysAsync(vale: V): Future[Set[K]]
}

trait AsyncDictionary[K, V] extends BaseAsyncDictionary[K, V] with AsyncDictionaryLike[K, V]

