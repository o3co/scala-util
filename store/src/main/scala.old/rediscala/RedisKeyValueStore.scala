package o3co.store.rediscala

import o3co.store.kvs.KeyValueStore
import o3co.exceptions._
import redis.ByteStringFormatter

/**
 */
trait RedisKeyValueStore[K, V] extends RedisStoreImpl[K] with KeyValueStore[K, V] {

  implicit def keySerialize: K => String
  implicit def keyDeserialize: String => K

  implicit def valueFormatter: ByteStringFormatter[V]
  
  def countAsync() = 
    redis.dbsize()

  def existsAsync(key: K) = 
    redis.exists(key)

  def getKeys() = 
    redis.keys("*").map { keys => 
      keys.map(k => k: K)
    }
  
  def getAsync(key: K) = 
    redis.get[V](key)

  def putAsync(key: K, value: V) = 
    redis.set(key, value).map {
      case false => throw new Exception("Failed to set value")
      case _ => //
    }

  def deleteAsync(key: K) = 
    redis.del(key).map {
      case 0 => throw new NotFoundException(s"Key $key is not exists.")
      case _ => //
    }
}
