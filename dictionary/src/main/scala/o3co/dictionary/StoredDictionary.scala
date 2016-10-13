package o3co.dictionary

import o3co.store.kvs.KeyValueStore

trait StoredDictionary[K, V] extends AsyncDictionary[K, V] {

  def store: KeyValueStore[K, V]

  def containsAsync(key: K) = {
    store.existsAsync(key)    
  }

  def containsValueAsync(value: V) = {
    //store.existsValueAsync(value)
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

  def keysAsync = {
    store.keysAsync    
  }

  def valuesAsync = {
    //store.valuesAsync    
    throw new Exception("Not impled")
  }
}

