package o3co.store.rediscala

import o3co.store.vs.ValueStore
import o3co.exceptions._
import redis.ByteStringFormatter

/**
 * Use redis SET api to manage values 
 */
trait RedisValueStoreImpl[V] extends RedisStoreImpl[String] with ValueStore[V] {

  /**
   * Key of set entry in Redis
   */
  def entryKey: String 

  implicit def valueFormatter: ByteStringFormatter[V]

  def countAsync() = 
    redis.scard(entryKey)

  def containsAsync(value: V) = 
    redis.sismember(entryKey, value)

  def putAsync(value: V) = {
    redis.sadd(entryKey, value)
      .map {
        case 0 => throw new DuplicatedException(s"Value $value is already existed.")
        case 1 => // Unit
      }
  }

  def deleteAsync(value: V) = {
    redis.srem(entryKey, value)
      .map {
        case 0 => throw new NotFoundException(s"Value $value is not exists.")
        case 1 => // Unit
      }
  }
}
