package o3co.dictionary

import scala.concurrent.Future

/**
 *
 */
trait Dictionary[K, V] extends BaseDictionary[K, V] {
  /**
   * Find keys for value 
   */
  def findKeys(value: V): Set[K]
}

/**
 *
 */
trait AsyncDictionary[K, V] extends BaseAsyncDictionary[K, V]{
  
  def findKeysAsync(vale: V): Future[Set[K]]
}

