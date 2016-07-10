package jp.o3co.id.store
package impl

import redis.RedisClient
import akka.actor.ActorRefFactory
import scala.concurrent.ExecutionContext

/**
 * Redis IDStore with Rediscala
 */
trait RedisIDStoreImpl[ID] extends IDStoreImpl[ID] {

  implicit def actorRefFactory: ActorRefFactory

  implicit def executionContext: ExecutionContext

  def key: String

  val redis = RedisClient()

  /**
   *
   */
  def existsAsync(id: ID) = {
    redis.sismember(key, id.toString)
  }

  /**
   *
   */
  def putAsync(id: ID) = {
    redis.sadd(key, id.toString)
      .map(_ => (): Unit)
  }

  /**
   *
   */
  def deleteAsync(id: ID) = {
    redis.srem(key, id.toString)    
      .map(_ => (): Unit)
  }
}

