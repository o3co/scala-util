package o3co.dictionary

import scala.concurrent.Future
import o3co.dictionary.store.BiDictionaryStore

trait StoredBiDictionary[K, V] extends AsyncBiDictionary[K, V] {

  def store: BiDictionaryStore[K, V]

  def containsAsync(key: K) = {
    store.existsAsync(key)    
  }

  def containsValueAsync(value: V) = {
    //store.existsValueAsync(value)
    throw new Exception("Not impled")
  }

  def getKeyAsync(value: V) = {
    throw new Exception("Not impled")   
  }
  
  def getAsync(key: K) = {
    store.getAsync(key)    
  }

  def putAsync(key: K, value: V) = {
    store.setAsync(key, value)    
  }

  def removeAsync(key: K) = {
    store.deleteAsync(key)    
  }

  def removeValueAsync(value: V) = {
    //store.deleteByValue(value)
    throw new Exception("Not impled")
  }

  def keysAsync = {
    store.keysAsync    
  }

  def valuesAsync = {
    //store.valuesAsync    
    throw new Exception("Not impled")
  }
}
