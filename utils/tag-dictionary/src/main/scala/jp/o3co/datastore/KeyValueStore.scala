package jp.o3co.datastore

import akka.actor.Actor
import akka.actor.ActorSelection
import akka.actor.ActorRefFactory
import akka.pattern.pipe
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

/**
 * KeyValueStore 
 */
trait KeyValueStore[Key, Value] {

  /**
   *
   */
  def containsAsync(key: Key): Future[Boolean]

  /**
   * @return Value if exists, None otherwise
   */
  def getAsync(key: Key): Future[Option[Value]]

  /**
   * @return Previous value related with Key
   */
  def putAsync(key: Key, value: Value): Future[Option[Value]]

  /**
   * @return Deleted value 
   */
  def deleteAsync(key: Key): Future[Option[Value]]
}

trait KeyValueStoreProtocol[Key, Value] {
  case class Contains(key: Key)
  case class ContainsComplete(exists: Boolean)
  case class ContainsFailure(cause: Throwable)

  case class Get(key: Key)
  case class GetComplete(value: Option[Value])
  case class GetFailure(cause: Throwable)

  case class Put(key: Key, value: Value)
  case class PutComplete(prev: Option[Value])
  case class PutFailure(cause: Throwable)

  case class Delete(key: Key)
  case class DeleteComplete(deleted: Option[Value])
  case class DeleteFailure(cause: Throwable)
}

trait KeyValueStoreActorLike[Key, Value] {
  this: Actor with KeyValueStore[Key, Value] => 

  val protocol: KeyValueStoreProtocol[Key, Value]

  import protocol._

  implicit val executionContext: ExecutionContext = context.dispatcher

  def receiveStoreCommand: Receive = {
    case Contains(key) => 
      containsAsync(key)
        .map(exists => ContainsComplete(exists))
        .recover {
          case e: Throwable => ContainsFailure(e)
        }
        .pipeTo(sender)
    case Get(key) => 
      getAsync(key)
        .map(value => GetComplete(value))
        .recover {
          case e: Throwable => GetFailure(e)
        }
        .pipeTo(sender)
    case Put(key, value) => 
      putAsync(key, value) 
        .map(prev => PutComplete(prev))
        .recover {
          case e: Throwable => PutFailure(e)
        }
        .pipeTo(sender)
    case Delete(key) => 
      deleteAsync(key)
        .map(deleted  => DeleteComplete(deleted))
        .recover {
          case e: Throwable => DeleteFailure(e)
        }
        .pipeTo(sender)
  }
}

trait KeyValueStoreAdapterLike[Key, Value] extends KeyValueStore[Key, Value] {
  val protocol: KeyValueStoreProtocol[Key, Value]
  import protocol._

  def endpoint: ActorSelection

  implicit def timeout: Timeout 

  implicit def executionContext: ExecutionContext

  def actorRefFactory: ActorRefFactory

  /**
   *
   */
  def containsAsync(key: Key) = {
    (endpoint ? Contains(key))
      .map {
        case ContainsComplete(exists) => exists
        case ContainsFailure(cause) => throw cause 
      }
  }

  /**
   * @return Value if exists, None otherwise
   */
  def getAsync(key: Key) = {
    (endpoint ? Get(key))
      .map {
        case GetComplete(exists) => exists
        case GetFailure(cause) => throw cause 
      }
  }

  /**
   * @return Previous value related with Key
   */
  def putAsync(key: Key, value: Value) = {
    (endpoint ? Put(key, value))
      .map {
        case PutComplete(exists) => exists
        case PutFailure(cause) => throw cause 
      }
  }

  /**
   * @return Deleted value 
   */
  def deleteAsync(key: Key) = {
    (endpoint ? Delete(key))
      .map {
        case DeleteComplete(exists) => exists
        case DeleteFailure(cause) => throw cause 
      }
  }
}
