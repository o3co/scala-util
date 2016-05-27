package jp.o3co.datastore
package kvs

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.ExecutionContext

trait KeyValueStoreActor[K, V] extends Actor with KeyValueStoreActorLike[K, V] {
  this: KeyValueStore[K, V] =>
  def receive = receiveKvsStoreCommand
}

trait KeyValueStoreActorLike[K, V] extends KeyValueStoreComponents[K, V] {
  this: Actor with KeyValueStore[K, V] => 

  val protocol: KeyValueStoreProtocolLike[K, V]

  import protocol._

  implicit val executionContext: ExecutionContext = context.dispatcher

  def receiveKvsStoreCommand: Receive = {
    case Contains(key) => 
      containsAsync(key)
        .map(exists => ContainsSuccess(exists))
        .recover {
          case e: Throwable => ContainsFailure(e)
        }
        .pipeTo(sender)
    case Get(key) => 
      getAsync(key)
        .map(value => GetSuccess(value))
        .recover {
          case e: Throwable => GetFailure(e)
        }
        .pipeTo(sender)
    case Put(key, value) => 
      putAsync(key, value) 
        .map(prev => PutSuccess(prev))
        .recover {
          case e: Throwable => PutFailure(e)
        }
        .pipeTo(sender)
    case Delete(key) => 
      deleteAsync(key)
        .map(deleted  => DeleteSuccess(deleted))
        .recover {
          case e: Throwable => DeleteFailure(e)
        }
        .pipeTo(sender)
  }
}

